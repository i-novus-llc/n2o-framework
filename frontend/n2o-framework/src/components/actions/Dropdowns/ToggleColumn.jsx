import React from 'react';
import PropTypes from 'prop-types';
import filter from 'lodash/filter';
import map from 'lodash/map';
import isArray from 'lodash/isArray';
import DropdownItem from 'reactstrap/lib/DropdownItem';
import { connect } from 'react-redux';
import { toggleColumnVisiblity } from '../../../actions/columns';
import { getContainerColumns } from '../../../selectors/columns';
import ChangeSize from './ChangeSize';

/**
 * Дропдаун для скрытия/показа колонок в таблице
 * @reactProps {string} widgetId - id виджета, размер которого меняется
 * @reactProps {array} columns - кологки(приходит из редакса)
 * @example
 * <ToggleColumn widgetId='TestWidgetId'/>
 */
class ToggleColumn extends React.Component {
  constructor(props) {
    super(props);
    this.toggleVisibility = this.toggleVisibility.bind(this);
  }

  /**
   * меняет видимость колонки по id
   * @param id
   */
  toggleVisibility(id) {
    const { dispatch, widgetId } = this.props;
    dispatch(toggleColumnVisiblity(widgetId, id));
  }

  /**
   * рендер дропдауна
   * @param columns
   */
  renderColumnDropdown(columns) {
    const notActive = (filter(columns, item => !item.value.visible) || []).map(
      col => col.key
    );
    return columns.map((column, i) => {
      const checked = !notActive.includes(column.key);
      return (
        <DropdownItem
          key={i}
          toggle={false}
          onClick={() => this.toggleVisibility(column.key)}
        >
          <span className="n2o-dropdown-check-container">
            {checked && <i className="fa fa-check" aria-hidden="true" />}
          </span>
          <span>{column.value.label || column.key}</span>
        </DropdownItem>
      );
    });
  }

  /**
   * Базовый рендер
   * @returns {*}
   */
  render() {
    const { columns } = this.props;
    const columnsArray = map(columns || {}, (value, key) => ({ key, value }));
    const filteredColumns = filter(
      columnsArray,
      ({ value }) => value.frozen !== true
    );

    return (
      <React.Fragment>
        {isArray(filteredColumns)
          ? this.renderColumnDropdown(filteredColumns)
          : null}
      </React.Fragment>
    );
  }
}

ToggleColumn.propTypes = {
  columns: PropTypes.object,
  widgetId: PropTypes.string,
};

const mapStateToProps = (state, props) => {
  return {
    columns: getContainerColumns(props.widgetId)(state),
  };
};

export { ToggleColumn };
export default connect(mapStateToProps)(ToggleColumn);
