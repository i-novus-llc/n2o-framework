import React from "react";
import StandardField from "n2o-framework/lib/components/widgets/Form/fields/StandardField/StandardField";
import RatingControl from "../RatingControl";

RatingControl.displayName = "Контрол: Рейтинг";
StandardField.displayName = "Контрол: Рейтинг";

export default [
  {
    component: RatingControl,
    name: "Рейтинг",
    props: {},
    reduxState: {}
  },
  {
    component: StandardField,
    name: "Рейтинг",
    props: {
      component: RatingControl,
      label: "Рейтинг",
      description: "Пример использования контрлда Рейтинг"
    },
    reduxState: {}
  }
];
