import SockJS from "sockjs-client";
import Stomp from "stompjs";
import { take, put, call, apply, delay, fork } from "redux-saga/effects";
import { eventChannel } from "redux-saga";

const token =
  "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJYUm5ONU01Y0pFMTlWNERtMnFuQ05KYWpwM0lsRGVYZl9QTk9fOWtKQXJvIn0.eyJqdGkiOiJjYTE4NzhjNC1hMDc2LTQ5ZTUtODhhYy1hN2QwZGI0MmFiZTMiLCJleHAiOjE1NTYxOTY2MzksIm5iZiI6MCwiaWF0IjoxNTU2MTY2NjM5LCJpc3MiOiJodHRwOi8vMTI3LjAuMC4xOjg4ODgvYXV0aC9yZWFsbXMvRE9NUkYiLCJhdWQiOiJsa2ItYXBwIiwic3ViIjoiMjJlY2RjZTMtNzllNS00NzFjLThiNjctZjUzNzJlNGY1NWQ1IiwidHlwIjoiQmVhcmVyIiwiYXpwIjoibGtiLWFwcCIsImF1dGhfdGltZSI6MTU1NjE2NjYzOSwic2Vzc2lvbl9zdGF0ZSI6ImJmNDU1NWUyLTQwMmYtNDdkOC1hNGNjLTc4NjZlYWMyZDc4MCIsImFjciI6IjEiLCJhbGxvd2VkLW9yaWdpbnMiOlsiaHR0cDovL2RvY2tlci5vbmU6ODgwOC8qIiwiaHR0cDovL2xvY2FsaG9zdDo4MDgwLyoiLCJodHRwOi8vZG9ja2VyLm9uZTo4ODA1LyoiLCJodHRwOi8vZG9ja2VyLm9uZTo4ODAzLyoiLCJodHRwOi8vZG9ja2VyLm9uZTo4ODA0LyoiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbIkRPTVJGX0RFVl9TUlYiLCIxMiIsIkFMTEJBTktTIiwiUk9MRV9ERVZFTE9QRVIiLCJ0ZXN0MTExMSIsIlJPTEVfRE9NUkYiLCJyZG0uYWRtaW4iLCJET01SRl9DVFJMX1NSViIsImFkbWluIiwidGVzdFJvbGUiLCJzZWMuYWRtaW4iLCJCQU5LQUNDIiwiRE9NX1JPTEUiLCJyb2xlLmFjY3JlZGl0YXRpb24iLCJ0ZXN0MyIsIjUiLCJET01SRl9GUk9OVEVORF9TUlYiLCJBRE1JTiIsInVtYV9hdXRob3JpemF0aW9uIiwiYWNjciIsImhhbmRtYWRlLm1vbml0b3JpbmcuYmFuayIsIlJPTEVfQkFOSyJdfSwicmVzb3VyY2VfYWNjZXNzIjp7InJlYWxtLW1hbmFnZW1lbnQiOnsicm9sZXMiOlsidmlldy1pZGVudGl0eS1wcm92aWRlcnMiLCJ2aWV3LXJlYWxtIiwibWFuYWdlLWlkZW50aXR5LXByb3ZpZGVycyIsImltcGVyc29uYXRpb24iLCJyZWFsbS1hZG1pbiIsImNyZWF0ZS1jbGllbnQiLCJtYW5hZ2UtdXNlcnMiLCJxdWVyeS1yZWFsbXMiLCJ2aWV3LWF1dGhvcml6YXRpb24iLCJxdWVyeS1jbGllbnRzIiwicXVlcnktdXNlcnMiLCJtYW5hZ2UtZXZlbnRzIiwibWFuYWdlLXJlYWxtIiwidmlldy1ldmVudHMiLCJ2aWV3LXVzZXJzIiwidmlldy1jbGllbnRzIiwibWFuYWdlLWF1dGhvcml6YXRpb24iLCJtYW5hZ2UtY2xpZW50cyIsInF1ZXJ5LWdyb3VwcyJdfSwiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwicm9sZXMiOlsicmRtLmFkbWluIiwib2ZmbGluZV9hY2Nlc3MiLCJBTExCQU5LUyIsIkRPTVJGX0NUUkxfU1JWIiwiQkFOS0FDQyIsIkRPTVJGX0RFVl9TUlYiLCJST0xFX0JBTksiLCJ0ZXN0Um9sZSIsInNlYy5hZG1pbiIsIkRPTV9ST0xFIiwiRE9NUkZfRlJPTlRFTkRfU1JWIiwiUk9MRV9ET01SRiIsIkFETUlOIiwidW1hX2F1dGhvcml6YXRpb24iLCJhY2NyIiwiaGFuZG1hZGUubW9uaXRvcmluZy5iYW5rIiwiYWRtaW4iLCJyb2xlLmFjY3JlZGl0YXRpb24iLCJST0xFX0RFVkVMT1BFUiIsInRlc3QxMTExIiwidGVzdDMiLCIxMiIsIjUiXSwicHJlZmVycmVkX3VzZXJuYW1lIjoiZWdvcnN0In0.RY9vR_vVYixSJkHioQpAd4L-UMxxcnsQq_-sySUFTKIPN-KugnUowknZ4Ki2oEupChCdgecLiToxIPOmuOcJl-F9N9wo1u_sS3k-DWSAUQeEtoKvY9Mbwj1n52P45reYkCXGJjONdbcpRH5zTn_Yciwx0u06loEVORrPGj1kEvekVnzQtiUHZMpQsyZVlOYXxvz0hTpywCPVa3K4gXb2BaRkaD-bQGkJ22QaEHev7gBl1zSpoS41ZV8SohmgiHwBR-Pi1NgQ9LWsLLBFCskX_c-dV9Eoo-XOwTptrvYrSj482smF4W6w6fnPlbbi2CdTFsFQGy_X3e50cDeSF20y6w";
const systemID = "sysId9";

function initWebsocket() {
  return eventChannel(emitter => {
    // dyn
    const socket = new SockJS(
      "http://docker.one:30675/ws/?access_token=" + token
    );
    const stompClient = Stomp.over(socket);

    stompClient.connect(
      {},
      frame => {
        console.log("Connected: " + frame);

        //Подписка на личные сообщения
        // dyn
        stompClient.subscribe(
          "/user/exchange/" + systemID + "/message",
          function(msg) {
            console.log(msg);
          }
        );

        //Подписка на счетчик
        // dyn
        stompClient.subscribe(
          "/user/exchange/" + systemID + "/message.count",
          function(msg) {
            console.log(msg);
          }
        );
      },
      errorCallback => {
        console.log(errorCallback);
      }
    );

    // emitter({ type: ADD_BOOK, book });

    // unsubscribe function
    const unsubscribe = () => {
      stompClient.disconnect();
      console.log("Disconnected");
    };

    return unsubscribe;
  });
}

export function* socketSaga() {
  debugger;
  const socketChannel = yield call(initWebsocket);
  while (true) {
    try {
      const payload = yield take(socketChannel);
      console.log("Socket done: " + payload);
    } catch (err) {
      console.error("socket error:", err);
      socketChannel.close();
    }
  }
}

export const sagas = [
  fork(socketSaga),
];
