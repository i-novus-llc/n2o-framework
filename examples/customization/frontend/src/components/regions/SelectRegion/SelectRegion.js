import React from "react";
import PropTypes from "prop-types";
import { compose } from "recompose";
import { FormGroup, Label, Input } from "reactstrap";
import { isEmpty, filter, map, pick, difference, pullAll, first } from "lodash";

import withGetWidget from "n2o/lib/components/regions/withGetWidget";
import Factory from "n2o/lib/core/factory/Factory";
import { WIDGETS } from "n2o/lib/core/factory/factoryLevels";
import SecurityCheck from "n2o/lib/core/auth/SecurityCheck";

/**
 *
 */
class SelectRegion extends React.Component {
  state = {
    widgetId: null
  };

  handleChange = e => {
    this.setWidgetId(e.target.value);
  };

  setWidgetId = widgetId => {
    if (widgetId !== this.state.widgetId) this.setState(() => ({ widgetId }));
  };

  /**
   * Рендер
   */
  render() {
    const { title, items, getWidget, pageId } = this.props;

    const { widgetId } = this.state;

    return (
      <div>
        <FormGroup>
          {title ? <Label>{title}</Label> : null}
          <Input type="select" name="changeRegion" onChange={this.handleChange}>
            {items.map(item => {
              if (item.active && !widgetId) this.setWidgetId(item.widgetId);
              return (
                <option
                  key={item.widgetId}
                  value={item.widgetId}
                  selected={item.active}
                >
                  {item.label}
                </option>
              );
            })}
          </Input>
        </FormGroup>
        <div>
          {widgetId ? (
            <Factory
              id={widgetId}
              level={WIDGETS}
              {...getWidget(pageId, widgetId)}
            />
          ) : (
            <span className="text-muted">Ничего не найдено</span>
          )}
        </div>
      </div>
    );
  }
}

SelectRegion.propTypes = {
  items: PropTypes.array.isRequired,
  getWidget: PropTypes.func.isRequired,
  pageId: PropTypes.string.isRequired,
  title: PropTypes.string
};

export default compose(withGetWidget)(SelectRegion);
