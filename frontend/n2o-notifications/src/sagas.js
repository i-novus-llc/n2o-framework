import SockJS from "sockjs-client";
import Stomp from "stompjs";
import { take, call, fork, select, put } from "redux-saga/effects";
import isEmpty from 'lodash/isEmpty';
import eq from 'lodash/eq';
import {
  requestConfigSuccess,
  userConfigSelector,
  localizationSelector
} from "n2o-framework/lib/ducks/global/store";
import { eventChannel } from "redux-saga";
import { setCounter, add } from "./actions";

const DEV_SERVER_URL = "/ws/?access_token=";

const getServerURL = url =>
  eq(process.env.NODE_ENV, "development") ? DEV_SERVER_URL : url;

export function* connectWS() {
  yield take(requestConfigSuccess.type);
  const user = yield select(userConfigSelector);
  const messagesURLs = yield select(localizationSelector);
  if (!isEmpty(user) && user.token && !isEmpty(messagesURLs)) {
    const socket = yield new SockJS(
      getServerURL(messagesURLs.wsUrl) + user.token
    );
    return Stomp.over(socket);
  }
  throw new Error("Not access token or not message url");
}

export function subscribeMessage(emit) {
  return msg => {
    const message = JSON.parse(msg.body);
    emit(add(message.id, message));
  };
}

export function subscribeMessageCount(emit) {
  return msg => {
    const message = JSON.parse(msg.body);
    emit(setCounter(message.id || "all", message.count));
  };
}

export function* createSocketChannel(stompClient) {
  const messagesURLs = yield select(localizationSelector);
  return eventChannel(emitter => {
    stompClient.connect(
      {},
      frame => {
        console.log("Connected: " + frame);

        //Подписка на личные сообщения
        stompClient.subscribe(
          messagesURLs.subscribeUrl,
          subscribeMessage(emitter)
        );

        //Подписка на счетчик
        stompClient.subscribe(
          messagesURLs.subscribeCountUrl,
          subscribeMessageCount(emitter)
        );

        stompClient.send(messagesURLs.getCountUrl);
      },
      errorCallback => {
        console.log(errorCallback);
      }
    );

    // unsubscribe function
    const unsubscribe = () => {
      stompClient.disconnect();
    };

    return unsubscribe;
  });
}

export function* socketSaga() {
  try {
    const stompClient = yield call(connectWS);
    const socketChannel = yield call(createSocketChannel, stompClient);
    while (true) {
      try {
        const action = yield take(socketChannel);
        yield put(action);
      } catch (err) {
        console.error("socket error:", err);
        socketChannel.close();
        call(socketSaga);
      }
    }
  } catch (e) {
    console.error(e);
  }
}

export const sagas = [fork(socketSaga)];
