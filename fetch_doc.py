from bs4 import BeautifulSoup
from urllib2 import urlopen
from urllib import unquote

html_template = """<html>
<head>
  <title>Weaving Simulator Help</title>
<style>
  body {
    font: 13px sans-serif;
}
</style>
</head>
<body>
  %s
</body>
</html>"""
url = "https://sourceforge.net/p/weavingsim/wiki/User%20Guide/"

print "Fetching..."
soup = BeautifulSoup(urlopen(url))

print "Writing text"
tags = soup.find_all("div", attrs={"class": "markdown_content"})
with open ("help.html", "w") as html_file:
    html_file.write(html_template % tags[0])

for img in soup.find_all("img"):
    name = img["src"]
    if "attachment" in name:
        print "Fetching %s ..." % name
        with open(unquote(name), "w") as img_file:
            img_file.write(urlopen(url + name).read())
