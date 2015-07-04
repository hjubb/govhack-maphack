#= Severity,Lon,Lat,Fatality,Hospitalised,MedicallyTreated,MinorInjury =#

data = readcsv("locations.csv")

severity = data[2:size(data,1)]
uniqueSeverity = unique(severity)

function severity2Int(s)
  if s == "Fatal"
    return 5
  elseif s == "Hospitalisation"
    return 4
  elseif s == "Medical treatment"
    return 3
  elseif s == "Minor injury"
    return 2
  else
    return 1
  end
end

sevint = map(severity2Int, severity)
lon = data[2:size(data,1),2]
lat = data[2:size(data,1),3]
fatal = data[2:size(data,1),4]
hospital = data[2:size(data,1),5]
medtreat = data[2:size(data,1),6]
injury = data[2:size(data,1),7]

function cumSeverity(s, f, h, m, i)
  return +(s, f, h, m, i)
end

totalSeverity = map(cumSeverity, sevint, fatal, hospital, medtreat, injury)

finalCsv = hcat(lon, lat, totalSeverity)

writecsv("weightedlocations.csv", finalCsv)
