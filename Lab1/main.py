from OpenThroughSocket import OpenThroughSocket
from DownloadImages import DownloadImages


def chooseOption(x):
    global host
    global port
    if x == 1:
        host = "me.utm.md"
        port = 80
    elif x == 2:
        host = "utm.md"
        port = 443
    else:
        print("Non-existent option")
        global option
        option = int(input("Enter the number of option: \n"))
        chooseOption(option)

option = None
host = ""
port = 0

if __name__ == '__main__':
    print("1) me.utm.md : 80")
    print("2) utm.md    : 443")
    option = int(input("Enter the number of option: \n"))

    chooseOption(option)
    list_of_links = OpenThroughSocket(host, int(port)).getLinks()
    DownloadImages(list_of_links, host, int(port)).startMultiThreading()
