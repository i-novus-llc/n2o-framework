import React from "react";
import controls from "n2o/lib/components/controls";
import FloatingLabelField from "../FloatingLabelField";

export default [
  {
    component: FloatingLabelField,
    name: "Плавающий лейбл",
    props: {
      id: "floatingField",
      control: {
        component: controls.InputText
      },
      label: "Ввод текста"
    },
    reduxState: {}
  }
];
