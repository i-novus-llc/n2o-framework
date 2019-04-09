import React from "react";
import { DatePicker } from "antd";
import { compose, defaultProps, withHandlers, withProps } from "recompose";
import moment from "moment";

const { RangePicker } = DatePicker;

export default compose(
  defaultProps({
    defaultTime: "11:11",
    timeFormat: "HH:mm",
    dateFormat: "DD/MM/YYYY",
    min: "5/12/2012",
    max: "15/12/2021",
    disabled: false,
    placeholder: "Введите дату",
    locale: "ru"
  }),
  withProps(props => {
    return {
      placeholder: [props.placeholder, props.placeholder],
      showTime: { hideDisabledOptions: true },
      value: props.value && [
        moment(props.value.begin, `${props.dateFormat} ${props.timeFormat}`),
        moment(props.value.end, `${props.dateFormat} ${props.timeFormat}`)
      ],
      style: { width: "100%" }
    };
  }),
  withHandlers({
    onChange: ({ data, valueFieldId, onChange, outputFormat }) => value => {
      onChange({
        begin: value[0].format(outputFormat),
        end: value[0].format(outputFormat)
      });
    },
    onBlur: props => value => {
      props.onBlur();
    }
  })
)(RangePicker);
