import React from "react";
import { DatePicker as BaseDatePicker } from "antd";
import { compose, defaultProps, withHandlers, withProps } from "recompose";
import moment from "moment";

export default compose(
  defaultProps({
    dateFormat: "DD/MM/YYYY",
    timeFormat: "hh:mm",
    outputFormat: "DD/MM/YYYY hh:mm",
    locale: "ru",
    placeholder: "",
    disabled: false,
    className: "",
    defaultTime: "00:00",
    autoFocus: false,
    value: null
  }),
  withProps(props => {
    return {
      value:
        props.value &&
        moment(props.value, `${props.dateFormat} ${props.timeFormat}`),
      style: { width: "100%" }
    };
  }),
  withHandlers({
    onChange: ({ data, valueFieldId, onChange, outputFormat }) => value => {
      onChange(value.format(outputFormat));
    },
    onBlur: props => value => {
      props.onBlur();
    }
  })
)(BaseDatePicker);
