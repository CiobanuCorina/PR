import socket
import re
import ssl
import io

class OpenThroughSocket:
    def __init__(self, host, PORT):
        self.host = host
        self.PORT = PORT
        self.dd = ""
        self.links_to_images = []

    def getLinks(self):
        with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as sock:
            sock.connect((self.host, self.PORT))
# Socket utm.md
            if self.PORT == 443:
                sock = ssl.wrap_socket(sock, keyfile=None, certfile=None, server_side=False, cert_reqs=ssl.CERT_NONE,
                                    ssl_version=ssl.PROTOCOL_SSLv23)
            request_header = 'GET / HTTP/1.0\r\nHOST: {}' \
                             '\r\nAccept: text/html' \
                             '\r\nConnection: keep-alive' \
                             '\r\nKeep-Alive: timeout=1, max=1000' \
                             '\r\nAccept-Language: ro,en' \
                             '\r\nAllow: GET' \
                             '\r\nDNT: 1' \
                             '\r\nSave-Data: on\r\n\r\n'.format(self.host).encode("utf-8")
            sock.send(request_header)
            self.dd = b''
            while True:
                temp = sock.recv(8048)
                if len(temp) == 0:
                    break
                else:
                    self.dd += temp
            sock.close()
        code_of_page = (self.dd.decode("utf-8"))
        with io.open('D:/Cora/univer/SemVII/PR/content.txt', "w", encoding="utf-8") as file:
            file.write(code_of_page)
        with io.open('D:/Cora/univer/SemVII/PR/content.txt', 'r', encoding="utf-8") as file:

# utm.md

            x = file.readlines()

            if self.PORT == 443:
                urls = []
                for line_in_file in x:
                    results = re.findall("[^\"']*\\.(?:png|jpg)", line_in_file)
                    for y in results:
                        if 'https://' not in y:
                            y = 'https://utm.md' + y
                        urls.append(y)
                urls = list(set(urls))
                self.links_to_images = urls

# me.utm.md

            if self.PORT == 80:
                timg = re.findall("[^\"']*\\.(?:png|jpg|gif)", str(x))
                if timg is not None:
                    self.links_to_images = timg

            if self.host == "me.utm.md":
                links = []
                for link in self.links_to_images:
                    if link.startswith("http://"):
                        links.append(link)
                    else:
                        links.append("http://me.utm.md/" + link)
                    self.links_to_images = links


            return self.links_to_images
