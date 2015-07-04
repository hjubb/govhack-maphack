#= Severity,Lon,Lat,Fatality,Hospitalised,MedicallyTreated,MinorInjury =#

data = readcsv("locations-all.csv")

severity = data[2:size(data,1),2]
lon = data[2:size(data,1),9]
lat = data[2:size(data,1),10]
surface = data[2:size(data,1),31]
lighting = data[2:size(data,1),33]
fatal = data[2:size(data,1),39]
hospital = data[2:size(data,1),40]
medtreat = data[2:size(data,1),41]
injury = data[2:size(data,1),42]

sevdict = {"Fatal"=>5,"Hospitalisation"=>4,"Medical treatment"=>3,"Minor injury"=>2,"Property damage only"=>1}

lightdict = {"Darkness - Not lighted"=>1, "Daylight"=>2, "Darkness - Lighted"=>3, "Dawn/Dusk"=>4, "Unknown"=>5}

surfacedict = {"Unsealed - Dry"=>1,"Sealed - Dry"=>2,"Sealed - Wet"=>3,"Unknown"=>4,"Unsealed - Wet"=>5}


function lightToInt(v)
  get(lightdict, v, 0)
end

function surfaceToInt(v)
  get(surfacedict, v, 0)
end

function severityToInt(v)
  get(sevdict, v, 0)
end

function cumSeverity(s, f, h, m, i)
  return +(s, f, h, m, i)
end

sevint = map(severityToInt, severity)
surfaceint = map(surfaceToInt, surface)
lightint = map(lightToInt, lighting)

totalSeverity = map(cumSeverity, sevint, fatal, hospital, medtreat, injury)

finalCsv = hcat(lon, lat, totalSeverity, surfaceint, lightint)

writecsv("weightedlocations.csv", finalCsv)
