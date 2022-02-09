#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <fcntl.h>
#include <sys/time.h>
#include <WinSock2.h>

char *concat(const char *a, const char *b){
    int lena = strlen(a);
    int lenb = strlen(b);
    char *con = malloc(lena+lenb+1);
    // copy & concat (including string termination)
    memcpy(con,a,lena);
    memcpy(con+lena,b,lenb);
    return con;
}

char *split(char *string, char *delimiter) {
    string = strstr(string, delimiter);
    return string + strlen(delimiter);
}

void socketConnect() {
    WSADATA WinSockData;
    int WsaStartup;
    int WsaCleanup;

    SOCKET ClientSocket;
    int CloseSocket;

    struct sockaddr_in ServerAddr;

    int Connect;

    int Recv;
    char RecvBuffer[512];
    int RecvBufferLen = strlen(RecvBuffer);

    int Send;
    char* SendBuffer = "GET /slide/large/2.jpg HTTP/1.1\r\n"
                       "Host: me.utm.md\r\n"
                       "\r\n";
    int SendBufferLen = strlen(SendBuffer) + 1;

    FILE* file;
    struct timeval timestamp;

    WsaStartup = WSAStartup(MAKEWORD(2,2), &WinSockData);
    if(WsaStartup != 0) {
        printf("WSAStartup failed\n");
    }
    printf("WSAStartup success\n");

    ClientSocket = socket(AF_INET, SOCK_STREAM, IPPROTO_TCP);
    if(ClientSocket == INVALID_SOCKET) {
        printf("TCP client socket creation failed\n");
    }
    printf("TCP client socket creation succeeded\n");

    struct hostent *host=gethostbyname("me.utm.md");
    char *ip=inet_ntoa(*(struct in_addr*)host->h_addr_list[0]);

    ServerAddr.sin_family = AF_INET;
    ServerAddr.sin_addr.s_addr = inet_addr(ip);
    ServerAddr.sin_port = htons(80);

    Connect = connect(ClientSocket, (SOCKADDR*) &ServerAddr, sizeof(ServerAddr));
    if(Connect == SOCKET_ERROR) {
        printf("Socket connection failed\n");
    }
    printf("Socket connected successfully\n");

    Send = send(ClientSocket, SendBuffer, SendBufferLen, 0);
    if(Send == SOCKET_ERROR) {
        printf("Data send unsuccessfully\n");
    }
    printf("Bytes Sent: %ld\n", Send);
    printf("Data send successfully\n");

    int count = 0;
    char* content;
    while(Recv = recv(ClientSocket, RecvBuffer, RecvBufferLen, 0) > 0) {
//        if(count == 0)
//            memcpy(content,RecvBuffer,RecvBufferLen - 1);
//        else
//            memcpy(content+(RecvBufferLen - 1),RecvBuffer,strlen(content) +1);
        content = concat(content, RecvBuffer);
        if(Recv == SOCKET_ERROR) {
            printf("Socket data receiving failed\n");
        }
//        printf(content);
//        printf("\n");
//        printf(RecvBuffer);
//        printf("\n");
        count++;
    }
//    printf(content);
//    content = split(content, "\r\n\r\n");
    content = strstr(content, "\r\n\r\n");
    char *result = content + 4;
    printf(result);
    printf("\n");
    printf("Data received successfully\n");

    gettimeofday(&timestamp, NULL);
    char time[255];
    sprintf(time, "%lu", timestamp.tv_sec * 1000000 + timestamp.tv_usec);

    file = fopen("D:\\Cora\\univer\\SemVII\\PR\\image1.jpg", "wb");
    fwrite(result, sizeof(result), strlen(result), file);
    fclose(file);

    CloseSocket = closesocket(ClientSocket);
    if(ClientSocket == SOCKET_ERROR) {
        printf("Couldn't close socket\n");
    }
    printf("Socket closed successfully\n");

    WsaCleanup = WSACleanup();
    free(content);
    if(WsaCleanup == SOCKET_ERROR) {
        printf("Cleanup failed\n");
    }
    printf("Successful cleanup\n");
}




