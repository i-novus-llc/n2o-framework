import React from "react";
import { describe, it } from "mocha";
import { expect } from "chai";
import { mount } from "enzyme";
import { Notifications } from "../src/Notifications";
import { Counter } from "../src/Counter";
import { Toast, ToastBody, ToastHeader, Badge } from 'reactstrap'

describe("Компоненты", () => {
  describe("Notifications", () => {
    it("создание", () => {
      const wrapper = mount(<Notifications />);
      expect(wrapper).to.be.exist;
    });
    it("map сообщений", () => {
      const wrapper = mount(
        <Notifications
          stack={[{ id: 1, text: "test" }, { id: 2, text: "test" }]}
        />
      );
      expect(wrapper.find(Toast).length).to.be.equal(2);
    });
    it("icon, title, text", () => {
      const wrapper = mount(
        <Notifications
          stack={[{ id: 1, text: "text", icon: "icon", title: "title" }]}
        />
      );
      expect(wrapper.find(ToastHeader).props().icon).to.be.equal("icon");
      expect(wrapper.find(ToastHeader).props().children).to.be.equal("title");
      expect(wrapper.find(ToastBody).props().children).to.be.equal("text");
    });
  });

  describe("Counter", () => {
    it("создание", () => {
      const wrapper = mount(<Counter />);
      expect(wrapper).to.be.exist;
    });
    it("props (color, count)", () => {
      const wrapper = mount(<Counter count={50} color="primary" />);
      expect(wrapper.find(Badge).props().color).to.be.equal("primary");
      expect(wrapper.find(Badge).props().children).to.be.equal(50);
    });
    it("props (noNumber=true)", () => {
      const wrapper = mount(<Counter noNumber={true} count={50} />);
      expect(wrapper.find(Badge).props().children).to.be.equal(null);
    });
    it("props (showZero=true, count=0)", () => {
      const wrapper = mount(<Counter showZero={true} count={0} />);
      expect(wrapper.find(Badge).props().children).to.be.equal(0);
    });
    it("props (count > overflowCount)", () => {
      const wrapper = mount(<Counter overflowCount={50} count={55} />);
      expect(wrapper.find(Badge).props().children).to.be.equal("50+");
    });
    it("props (overflowText)", () => {
      const wrapper = mount(
        <Counter overflowCount={50} count={55} overflowText="test" />
      );
      expect(wrapper.find(Badge).props().children).to.be.equal("test");
    });
  });
});
