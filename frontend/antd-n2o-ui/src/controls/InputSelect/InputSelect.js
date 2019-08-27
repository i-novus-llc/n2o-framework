import React from "react";
import { Select, Icon, Avatar, Badge } from "antd";
import { withProps, withHandlers, compose, defaultProps } from "recompose";
import { map, isArray, filter, find } from "lodash";
import listContainer from "n2o-framework/lib/components/controls/listContainer";

export default compose(
  listContainer,
  defaultProps({
    loading: false,
    disabled: false,
    placeholder: "Введите значение",
    valueFieldId: "id",
    labelFieldId: "label",
    iconFieldId: "icon",
    imageFieldId: "image",
    badgeFieldId: "badge",
    badgeColorFieldId: "color",
    groupFieldId: "",
    filter: "server",
    resetOnBlur: false,
    queryId: "list",
    multiSelect: false
  }),
  withProps(
    ({
      data,
      valueFieldId,
      labelFieldId,
      iconFieldId,
      badgeFieldId,
      value
    }) => ({
      children: map(data, (item, index) => (
        <Select.Option key={index} value={item[valueFieldId]}>
          {item[iconFieldId] && <Icon type="step-backward" />}
          {item[iconFieldId] && (
            <Avatar size="small" shape="square" src={item[iconFieldId]} />
          )}
          {item[labelFieldId]}
          {item[badgeFieldId] && (
            <Badge status="error" text={item[badgeFieldId]} />
          )}
        </Select.Option>
      )),
      style: { width: "100%" },
      value: isArray(value) ? map(value, valueFieldId) : value[valueFieldId]
    })
  ),
  withHandlers({
    onChange: ({ data, valueFieldId, onChange }) => value => {
      console.log(value);
      if (!value) {
        onChange(null);
      } else if (isArray(value)) {
        onChange(filter(data, dt => value.includes(dt[valueFieldId])));
      } else {
        onChange(find(data, [valueFieldId, value]));
      }
    },
    onDropdownVisibleChange: props => visible => {
      if (visible) {
        props._fetchData();
      }
    },
    onBlur: props => () => {
      //props.onBlur(null);
    }
  })
)(Select);
