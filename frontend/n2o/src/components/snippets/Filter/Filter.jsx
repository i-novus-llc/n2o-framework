import React from 'react';
import PropTypes from 'prop-types';
import { FormattedMessage } from 'react-intl';
import cx from 'classnames';
import Buttons from './Buttons';

/**
 * Компонент фильтр (Filter)
 * @reactProps {node} children - элемент управления
 * @reactProps {object} style - кастомные стили
 * @reactProps {function} onSearch - callback на поиск
 * @reactProps {function} onReset - callback на сброс
 * @reactProps {array} filters
 * @reactProps {string} className - класс корневого элемента
 * @reactProps {string} searchLabel - лейбел кнопки поиска
 * @reactProps {string} resetLabel - лейбел кнопки сброса
 * @reactProps {boolean} visible - флаг видимости
 * @example
 * <Link to="/path/1" onClick={this.changeUrl}>Ссылка</Link>
 */
class Filter extends React.Component {
  constructor(props) {
    super(props);
    this.onReset = this.onReset.bind(this);
    this.onSearch = this.onSearch.bind(this);
  }

  onReset() {
    this.props.onReset();
  }

  onSearch() {
    this.props.onSearch();
  }

  render() {
    const {
      className,
      style,
      visible,
      resetLabel,
      searchLabel,
      hideButtons,
      children,
    } = this.props;

    return visible ? (
      <div className={cx('n2o-filter', className)} style={style}>
        {children}
        {!hideButtons ? (
          <Buttons
            searchLabel={searchLabel}
            resetLabel={resetLabel}
            onSearch={this.onSearch}
            onReset={this.onReset}
          />
        ) : null}
      </div>
    ) : null;
  }
}

Filter.propTypes = {
  children: PropTypes.node,
  style: PropTypes.object,
  onSearch: PropTypes.func,
  onReset: PropTypes.func,
  className: PropTypes.string,
  filters: PropTypes.array,
  searchLabel: PropTypes.string,
  resetLabel: PropTypes.string,
  visible: PropTypes.bool,
  hideButtons: PropTypes.bool,
};

Filter.defaultProps = {
  onSearch: () => {},
  onReset: () => {},
  filters: [],
  visible: true,
  style: {},
  className: '',
  searchLabel: 'Найти',
  resetLabel: 'Сбросить',
  hideButtons: false,
};

export default Filter;
