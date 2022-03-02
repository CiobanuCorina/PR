import socket
import ssl
import threading
from threading import Thread
from urllib.parse import urlparse
import os

sem = threading.Semaphore(2)

class DownloadImages(Thread):
    def __init__(self, IMAGESLIST, HOST, PORT):
        Thread.__init__(self)
        self.HOST = HOST
        self.IMAGESLIST = IMAGESLIST
        self.PORT = PORT
        self.array1 = []
        self.array2 = []
        self.array3 = []
        self.array4 = []

    def divideList(self):
        first_array = []
        second_array = []
        for i in range(len(self.IMAGESLIST)):
            if (i%2==0):
                first_array.append(self.IMAGESLIST[i])
            else:
                second_array.append(self.IMAGESLIST[i])
        for i in range(len(first_array)):
            if (i % 2 == 0):
                self.array1.append(first_array[i])
            else:
                self.array2.append(first_array[i])
        for i in range(len(second_array)):
            if (i % 2 == 0):
                self.array3.append(second_array[i])
            else:
                self.array4.append(second_array[i])

    def downloadThroughSockets(self, name, list_of_images):
        sem.acquire()
        for i in list_of_images[0]:
            s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
            s.connect((self.HOST, self.PORT))
            p = urlparse(i)
# pentru utm.md
            if self.PORT == 443:
                s = ssl.wrap_socket(s, keyfile=None, certfile=None, server_side=False, cert_reqs=ssl.CERT_NONE,
                                    ssl_version=ssl.PROTOCOL_SSLv23)
            request_header = 'GET {} HTTP/1.1\r\nHOST: {}\r\n\r\n'.format(p.path, self.HOST).encode()
            s.send(request_header)

            images = b''
            while True:
                data = s.recv(1024) 
                if not data:
                    images = images.split(b"\r\n\r\n")  
                    image_path = "D:/Cora/univer/SemVII/PR/img" + os.path.basename(p.path)
                    with open(image_path, "wb") as fcont:
                        fcont.write(images[-1])
                    break
                images += data
            print(threading.current_thread())
            s.close()
        sem.release()

    def startMultiThreading(self):
        thread_list = []

        self.divideList()
        t1 = Thread(target=self.downloadThroughSockets, args=(1, [self.array1],))
        t2 = Thread(target=self.downloadThroughSockets, args=(2, [self.array2],))
        t3 = Thread(target=self.downloadThroughSockets, args=(3, [self.array3],))
        t4 = Thread(target=self.downloadThroughSockets, args=(4, [self.array4],))
        thread_list.append(t1)
        thread_list.append(t2)
        thread_list.append(t3)
        thread_list.append(t4)
        t1.start()
        t2.start()
        t3.start()
        t4.start()
        for thread in thread_list:
            thread.join()
