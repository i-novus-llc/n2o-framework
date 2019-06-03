import React from "react";
import PropTypes from "prop-types";
import Resizable from "re-resizable";
import layoutPlaceResolver from "n2o/lib/components/layouts/LayoutPlaceResolver";
import Place from "n2o/lib/components/layouts/Place";
import "./SquareResizable.scss";

/**
 *
 */
class SquareLayout extends React.Component {
  render() {
    return (
      <div className="n2o-square-resizable-layout">
        <div className="n2o-square-resizable-layout_row">
          <Resizable
            className="n2o-square-resizable-layout_place"
            defaultSize={{
              width: "100%"
            }}
          >
            <Place name="topLeft" />
          </Resizable>
          <Resizable
            className="n2o-square-resizable-layout_place"
            defaultSize={{
              width: "100%"
            }}
          >
            <Place name="topRight" />
          </Resizable>
        </div>
        <div className="n2o-square-resizable-layout_row">
          <Resizable
            className="n2o-square-resizable-layout_place"
            defaultSize={{
              width: "100%"
            }}
          >
            <Place name="bottomLeft" />
          </Resizable>
          <Resizable
            className="n2o-square-resizable-layout_place"
            defaultSize={{
              width: "100%"
            }}
          >
            <Place name="bottomRight" />
          </Resizable>
        </div>
      </div>
    );
  }
}

export default layoutPlaceResolver(SquareLayout);
