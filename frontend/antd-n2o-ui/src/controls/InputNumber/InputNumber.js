import React from 'react';
import { InputNumber as BaseNumberBase } from "antd";

export default ({ style, ...rest }) => (
  <BaseNumberBase style={{ width: "100%", ...style }} {...rest} />
);
