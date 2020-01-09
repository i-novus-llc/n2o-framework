import React from "react";
import { describe, it } from "mocha";
import { expect } from "chai";
import {
  connectWS,
  subscribeMessage,
  subscribeMessageCount,
  createSocketChannel
} from "../src/sagas";
import SockJS from "sockjs-client";
import { add, setCounter } from "../src/actions";

const fakeURL = "http://localhost:8080";

describe("Тест саги для websocket", () => {
  let next;
  describe("connectWS", () => {
    it("есть токен", () => {
      const generator = connectWS();
      next = generator.next();
      next = generator.next();
      next = generator.next({ token: "test-tocken" });
      next = generator.next({ wsUrl: fakeURL });
      next = generator.next(new SockJS(fakeURL));
      expect(generator.next().done).to.be.equal(true);
    });
    it("нет токена", () => {
      const generator = connectWS();
      next = generator.next();
      next = generator.next({ token: "test-tocken" });
      next = generator.next({ wsUrl: fakeURL });
      expect(() => generator.next({}).value).to.throw("Not access token");
    });
  });

  describe("Обработка сообщения", () => {
    it("subscribeMessage", () => {
      const msg = { body: JSON.stringify({ text: "testMessage", id: 1 }) };
      const emitFn = subscribeMessage(res => {
        expect(res).to.be.deep.equal(add(1, { text: "testMessage" }));
      });
      emitFn(msg);
    });
    it("subscribeMessageCount", () => {
      const msg = { body: JSON.stringify({ count: 12 }) };
      const emitFn = subscribeMessageCount(res => {
        expect(res).to.be.deep.equal(setCounter("all", 12));
      });
      emitFn(msg);
    });
  });
  describe("event listener", () => {
    it("createSocketChannel", () => {
      const createChannel = createSocketChannel({
        connect: a => {
          expect(a).to.be.exist;
        }
      });
      createChannel.next();
      const eventChannet = createChannel.next({
        subscribeUrl: "test",
        subscribeCountUrl: "test"
      });
      expect(eventChannet.done).to.be.equal(true);
    });
  });
});
