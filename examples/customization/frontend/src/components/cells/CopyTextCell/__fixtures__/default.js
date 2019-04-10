import React from "react";
import CopyTextCell from "../CopyTextCell";

export default [
  {
    component: CopyTextCell,
    name: "Ячейка текст которой можно скопировать",
    props: {
      model: {
        name:
          "Предвижу всё: вас оскорбит\nПечальной тайны объясненье.\nКакое горькое презренье\nВаш гордый взгляд изобразит!\nЧего хочу? с какою целью\nОткрою душу вам свою?\nКакому злобному веселью,\nБыть может, повод подаю!"
      },
      fieldKey: "name",
      visible: true
    },
    reduxState: {}
  }
];
