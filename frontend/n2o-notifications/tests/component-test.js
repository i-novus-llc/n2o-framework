import React from "react";
import expect from "expect";
import { mount, shallow } from "enzyme";
import { Notifications } from "../src/Notifications";
import { Counter } from "../src/Counter";
import Toast from "reactstrap/lib/Toast";
import ToastBody from "reactstrap/lib/ToastBody";
import ToastHeader from "reactstrap/lib/ToastHeader";
import Badge from "reactstrap/lib/Badge";

describe("Компоненты", () => {
  describe("Notifications", () => {
    it("создание", () => {
      const wrapper = mount(<Notifications />);
      expect(wrapper).toBeTruthy();
    });
    it("map сообщений", () => {
      const wrapper = mount(
        <Notifications
          stack={[{ id: 1, text: "test" }, { id: 2, text: "test" }]}
        />
      );
      expect(wrapper.find(Toast).length).toBe(2);
    });
    it("icon, title, text", () => {
      const wrapper = mount(
        <Notifications
          stack={[{ id: 1, text: "text", icon: "icon", title: "title" }]}
        />
      );
      expect(wrapper.find(ToastHeader).props().icon).toBe("icon");
      expect(wrapper.find(ToastHeader).props().children).toBe("title");
      expect(wrapper.find(ToastBody).props().children).toBe("text");
    });
  });

  describe("Counter", () => {
    it("создание", () => {
      const wrapper = mount(<Counter />);
      expect(wrapper).toBeTruthy();
    });
    it("props (color, count)", () => {
      const wrapper = mount(<Counter count={50} color="primary" />);
      expect(wrapper.find(Badge).props().color).toBe("primary");
      expect(wrapper.find(Badge).props().children).toBe(50);
    });
    it("props (noNumber=true)", () => {
      const wrapper = mount(<Counter noNumber={true} count={50} />);
      expect(wrapper.find(Badge).props().children).toBe(null);
    });
    it("props (showZero=true, count=0)", () => {
      const wrapper = mount(<Counter showZero={true} count={0} />);
      expect(wrapper.find(Badge).props().children).toBe(0);
    });
    it("props (count > overflowCount)", () => {
      const wrapper = mount(<Counter overflowCount={50} count={55} />);
      expect(wrapper.find(Badge).props().children).toBe("50+");
    });
    it("props (overflowText)", () => {
      const wrapper = mount(
        <Counter overflowCount={50} count={55} overflowText="test" />
      );
      expect(wrapper.find(Badge).props().children).toBe("test");
    });
  });
});
