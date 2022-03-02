#include <stdio.h>
#include "socket.c"
#include "socketConnect.c"
#include "imagesList.c"

int main() {
    int bytesCount = socketConnectWebsite();
//    imagesList(bytesCount);
    socketConnect();
    return 0;
}
