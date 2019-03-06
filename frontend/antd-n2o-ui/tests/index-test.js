import expect from "expect";
import React from "react";
import { render, unmountComponentAtNode } from "react-dom";

import Component from "frontend/antd-n2o-ui/tests/index-test";

describe("Component", () => {
  let node;

  beforeEach(() => {
    node = document.createElement("div");
  });

  afterEach(() => {
    unmountComponentAtNode(node);
  });

  it("displays a welcome message", () => {
    render(<Component />, node, () => {
      expect(node.innerHTML).toContain("Welcome to React components");
    });
  });
});
