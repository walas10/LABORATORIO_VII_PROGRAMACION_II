$ErrorActionPreference = 'Stop'
Add-Type -AssemblyName System.Net.Http
$workspace = Split-Path $PSScriptRoot -Parent
$env:LAB_API_KEY = [guid]::NewGuid().ToString('N')
$client = New-Object System.Net.Http.HttpClient
$client.Timeout = [TimeSpan]::FromSeconds(2)
function Request($method, $url, $body, $key, $expected) {
    $request = New-Object System.Net.Http.HttpRequestMessage -ArgumentList $method, $url
    if ($key) { $request.Headers.Add('X-API-KEY', $key) }
    if ($null -ne $body) { $request.Content = New-Object System.Net.Http.StringContent -ArgumentList $body, ([Text.Encoding]::UTF8), 'application/json' }
    $response = $client.SendAsync($request).GetAwaiter().GetResult()
    $text = $response.Content.ReadAsStringAsync().GetAwaiter().GetResult()
    $status = [int]$response.StatusCode
    $request.Dispose()
    $response.Dispose()
    if ($status -ne $expected) { throw "$method $url esperaba $expected recibio ${status}: $text" }
    Write-Host "$method $url -> $status"
    if ($text) { return ($text | ConvertFrom-Json) }
}
try {
    $projects = @(
        @('Ejercicio1', 'profesores', 'especialidad', 'Matematicas', 8081),
        @('Ejercicio2', 'proveedores', 'categoria', 'Tecnologia', 8082),
        @('Ejercicio3', 'empleados', 'especialidad', 'Sistemas', 8083)
    )
    foreach ($p in $projects) {
        $project = Join-Path $workspace "$($p[0])/$($p[1])-api"
        $jar = Join-Path $project "target/$($p[1])-api-0.0.1-SNAPSHOT.jar"
        $process = Start-Process java -ArgumentList @('-jar', ('"' + $jar + '"')) -WindowStyle Hidden -PassThru -RedirectStandardOutput "$PSScriptRoot/$($p[1]).log" -RedirectStandardError "$PSScriptRoot/$($p[1])-error.log"
        try {
            $url = "http://localhost:$($p[4])/$($p[1])"
            $ready = $false
            for ($i = 0; $i -lt 60; $i++) {
                if ($process.HasExited) { throw "La aplicacion $($p[1]) termino. Revisar log." }
                try { $null = Request GET $url $null $null 401; $ready = $true; break } catch { Start-Sleep -Milliseconds 500 }
            }
            if (!$ready) { throw 'Tiempo de inicio agotado' }
            $null = Request GET $url $null 'incorrecta' 401
            $null = Request POST $url '{}' $null 401
            $null = Request GET $url $null $env:LAB_API_KEY 200
            $body = @{id=999;nombre='Ana';apellido='Lopez';correo='ana@example.com';estado='ACTIVO'}
            $body[$p[2]] = $p[3]
            $created = Request POST $url ($body | ConvertTo-Json) $env:LAB_API_KEY 201
            if ($created.id -eq 999 -or !$created.id) { throw 'ID no generado' }
            $null = Request GET "$url/$($created.id)" $null $env:LAB_API_KEY 200
            $matches = @(Request GET "${url}?$($p[2])=$($p[3])" $null $env:LAB_API_KEY 200)
            if ($matches.Count -ne 1) { throw 'Filtro coincidente incorrecto' }
            $misses = @(Request GET "${url}?$($p[2])=Otra" $null $env:LAB_API_KEY 200)
            if ($misses.Count -ne 0) { throw 'Filtro sin coincidencias incorrecto' }
            $null = Request GET "$url/99999" $null $env:LAB_API_KEY 404
            $null = Request GET "$url/texto" $null $env:LAB_API_KEY 400
            $null = Request POST $url '{}' $env:LAB_API_KEY 400
            $body.correo = 'invalido'
            $null = Request POST $url ($body | ConvertTo-Json) $env:LAB_API_KEY 400
            $body.correo = ''; $body.nombre = ' '; $body.apellido = ''
            $null = Request POST $url ($body | ConvertTo-Json) $env:LAB_API_KEY 400
            $null = Request POST $url '{' $env:LAB_API_KEY 400
        } finally {
            if (!$process.HasExited) { Stop-Process -Id $process.Id }
        }
    }
} finally {
    $client.Dispose()
    Remove-Item Env:LAB_API_KEY
}
Write-Host 'Verificacion HTTP completada en los tres proyectos.'
