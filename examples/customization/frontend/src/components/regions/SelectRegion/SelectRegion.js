import React, { Component } from "react";
import PropTypes from "prop-types";
import { compose } from "recompose";
import FormGroup from "reactstrap/lib/FormGroup";
import Label from "reactstrap/lib/Label";
import Input from "reactstrap/lib/Input";

import withWidgetProps from "n2o-framework/lib/components/regions/withWidgetProps";
import Factory from "n2o-framework/lib/core/factory/Factory";
import { WIDGETS } from "n2o-framework/lib/core/factory/factoryLevels";

class SelectRegion extends Component {
  constructor(props) {
    super();
    const { content } = props;
    const widget = content.find(widget => widget.active);
    this.state = {
      widgetId: widget && widget.id
    };
  }

  handleChange = e => {
    const { widgetId: widgetIdFromState } = this.state;
    const { value: widgetId } = e.target;
    if (widgetId !== widgetIdFromState) this.setState(prev => ({ ...prev, widgetId }));
  };

  render() {
    const { title, content } = this.props;
    const { widgetId } = this.state;

    return (
        <div>
          <FormGroup>
            {title ? <Label>{title}</Label> : null}
            <Input
                type="select"
                name="changeRegion"
                value={widgetId}
                onChange={this.handleChange}
            >
              {content.map(widget => {
                return (
                    <option
                        key={widget.id}
                        value={widget.id}
                        selected={widget.active}
                    >
                      {widget.label}
                    </option>
                );
              })}
            </Input>
          </FormGroup>
          <div>
            {content.map(widget => {
              return (
                  <div
                      key={widget.id}
                      style={{
                        display: widgetId === widget.id ? "block" : "none"
                      }}
                  >
                    <Factory
                        src={widget.src}
                        id={widget.id}
                        level={WIDGETS}
                        {...widget}
                    />
                  </div>
              );
            })}
          </div>
        </div>
    );
  }
}

SelectRegion.propTypes = {
  content: PropTypes.array.isRequired,
  getWidget: PropTypes.func.isRequired,
  pageId: PropTypes.string.isRequired,
  title: PropTypes.string
};

export default compose(withWidgetProps)(SelectRegion);
