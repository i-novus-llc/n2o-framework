import React from "react";
import { DatePicker as BaseDatePicker } from "antd";
import { compose, defaultProps, withHandlers, withProps } from "recompose";
import moment from "moment";
import { filter, find, isArray } from "lodash";

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
    autoFocus: false
  }),
  withProps(props => {
    return {
      value: moment(props.value).format(
        `${props.dateFormat} ${props.timeFormat}`
      )
    };
  }),
  withHandlers({
    onChange: ({ data, valueFieldId, onChange }) => value => {
      onChange();
    }
  })
)(BaseDatePicker);
