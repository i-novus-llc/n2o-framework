import React from 'react';
import PropTypes from 'prop-types';
import _ from 'lodash';
import { DropdownItem } from 'reactstrap';
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
    const notActive = (_.filter(columns, { isVisible: false }) || []).map(col => col.columnId);
    return columns.map(column => {
      const checked = !notActive.includes(column.columnId);
      return (
        <DropdownItem toggle={false} onClick={() => this.toggleVisibility(column.columnId)}>
          <span className="n2o-dropdown-check-container">
            {checked && <i className="fa fa-check" aria-hidden="true" />}
          </span>
          <span>{column.label || column.columnId}</span>
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
    const columnsArray = Object.values(columns || {});
    return (
      <React.Fragment>
        {_.isArray(columnsArray) ? this.renderColumnDropdown(columnsArray) : null}
      </React.Fragment>
    );
  }
}

ToggleColumn.propTypes = {
  columns: PropTypes.object,
  widgetId: PropTypes.string
};

const mapStateToProps = (state, props) => {
  return {
    columns: getContainerColumns(props.widgetId)(state)
  };
};

ToggleColumn = connect(mapStateToProps)(ToggleColumn);
export default ToggleColumn;
