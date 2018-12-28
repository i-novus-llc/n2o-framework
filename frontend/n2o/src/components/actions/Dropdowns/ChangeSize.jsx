import React from 'react';
import PropTypes from 'prop-types';
import { DropdownItem } from 'reactstrap';
import { connect } from 'react-redux';

import { changeSizeWidget, dataRequestWidget } from '../../../actions/widgets';
import { makeWidgetSizeSelector } from '../../../selectors/widgets';

/**
 * Дропдаун для выбора размера(size) виджета
 * @reactProps {string} widgetId - id виджета, размер которого меняется
 * @reactProps {number} size - текущий размер(приходит из редакса)
 * @example
 * <ChangeSize widgetId='TestWidgetId'/>
 */
class ChangeSize extends React.Component {
  constructor(props) {
    super(props);
    this.sizes = [5, 10, 20, 50];
    this.resize = this.resize.bind(this);
  }

  /**
   * изменение размера
   * @param size
   */
  resize(size) {
    const { dispatch, widgetId } = this.props;
    dispatch(changeSizeWidget(widgetId, size));
    dispatch(dataRequestWidget(widgetId, { size }));
  }

  /**
   * рендер меню
   * @param sizes
   */
  renderSizeDropdown(sizes) {
    const { size } = this.props;
    return sizes.map(s => {
      return (
        <DropdownItem toggle={false} onClick={() => this.resize(s)}>
          <span className="n2o-dropdown-check-container">
            {size === s && <i className="fa fa-check" aria-hidden="true" />}
          </span>
          <span>{s}</span>
        </DropdownItem>
      );
    });
  }

  /**
   * базовый рендер
   * @returns {*}
   */
  render() {
    return <React.Fragment>{this.renderSizeDropdown(this.sizes)}</React.Fragment>;
  }
}

ChangeSize.propTypes = {
  size: PropTypes.number,
  widgetId: PropTypes.string
};

const mapStateToProps = (state, props) => {
  return {
    size: makeWidgetSizeSelector(props.widgetId)(state)
  };
};

ChangeSize = connect(mapStateToProps)(ChangeSize);
export default ChangeSize;
