import React from "react";
import sinon from "sinon";

import { CheckboxGroup } from "../../inputs/CheckboxGroup";

const setup = (groupOverrides) => {
    const groupProps = {
        // use this to assign some default props

        ...groupOverrides,
    };

    const wrapper = mount(<CheckboxGroup {...groupProps} options={[{ id: 1 }, { id: 2 }]} />);

    return {
        groupProps,
        wrapper,
    };
};

describe("<CheckboxGroup />", () => {
    it("creates checkboxes ", () => {
        const { wrapper } = setup();
        expect(wrapper.find('input[type="checkbox"]')).toHaveLength(2);
    });

    it("sets properties properly", () => {
        const { wrapper } = setup({ name: "name" });
        expect(wrapper.find(CheckboxGroup).props().name).toBe("name");
    });

    it("sets properties to input properly", () => {
        const { wrapper } = setup();
        expect(wrapper.find('input[type="checkbox"]').first().props().value).toEqual({ id: 1 });
    });

    it("calls onChange", () => {
        const onChange = sinon.spy();
        const { wrapper } = setup({ onChange });
        wrapper
            .find('input[type="checkbox"]')
            .first()
            .simulate("change", { target: { checked: true } });
        expect(onChange.calledOnce).toBe(true);
    });

    it("calls onFocus", () => {
        const onFocus = sinon.spy();
        const { wrapper } = setup({ onFocus });
        wrapper.find('input[type="checkbox"]').at(0).simulate("focus");
        expect(onFocus.calledOnce).toBe(true);
    });

    it("calls onBlur", () => {
        const onBlur = sinon.spy();
        const { wrapper } = setup({ onBlur });
        wrapper.find('input[type="checkbox"]').first().simulate("blur");
        expect(onBlur.calledOnce).toBe(true);
    });

    it("checks value changes", () => {
        const { wrapper } = setup({
            value: [{ id: 1 }, { id: 2 }],
            valueFieldId: "id",
        });
        expect(wrapper.find('input[type="checkbox"]').at(0).props().checked).toBe(true);
        expect(wrapper.find('input[type="checkbox"]').at(1).props().checked).toBe(true);

        wrapper.setProps({ value: [{ id: 1 }] });
        wrapper.update();
        expect(wrapper.find('input[type="checkbox"]').at(0).props().checked).toBe(true);
        expect(wrapper.find('input[type="checkbox"]').at(1).props().checked).toBe(false);

        // value null
        wrapper.setProps({ value: null });
        wrapper.update();
        expect(wrapper.find('input[type="checkbox"]').at(0).props().checked).toBe(false);
        // value undefined
        wrapper.setProps({ value: undefined });
        wrapper.update();
        expect(wrapper.find('input[type="checkbox"]').at(0).props().checked).toBe(false);
        // value empty array
        wrapper.setProps({ value: [] });
        wrapper.update();
        expect(wrapper.find('input[type="checkbox"]').at(0).props().checked).toBe(false);
    });
});
