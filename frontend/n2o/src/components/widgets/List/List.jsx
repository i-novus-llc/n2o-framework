import React, { Component } from 'react';
import cn from 'classnames';
import { map } from 'lodash';
import PropTypes from 'prop-types';
import ListItem from './ListItem';
import ListMoreButton from './ListMoreButton';
import { List as Virtualizer } from 'react-virtualized';

const listHeight = 600;
const rowHeight = 50;

/**
 * Компонент List
 * @reactProps {number|string} selectedId - id выбранной записи
 * @reactProps {boolean} autoFocus - фокус при инициализации на перой или на selectedId строке
 * @reactProps {function} onItemClick - callback при клике на строку
 * @reactProps {object} rowClick - кастомное действие клика
 * @reactProps {function} onFetchMore - callback при клика на "Загрузить еще" или скролле
 */
class List extends Component {
  constructor(props) {
    super(props);

    this.state = {
      selectedIndex: props.selectedId || (props.autoSelect ? 0 : null),
      rowWidth: 0
    };

    this._scrollTimeoutId = null;

    this.renderRow = this.renderRow.bind(this);
    this.onItemClick = this.onItemClick.bind(this);
    this.fetchMore = this.fetchMore.bind(this);
    this.onScroll = this.onScroll.bind(this);
    this.setListContainerRef = this.setListContainerRef.bind(this);
  }

  componentDidMount() {
    this._listContainer.addEventListener('scroll', this.onScroll, true);
    this.setState({
      rowWidth: this._listContainer.clientWidth
    });
  }

  componentWillUnmount() {
    this._listContainer.removeEventListener('scroll', this.onScroll);
  }

  setListContainerRef(el) {
    this._listContainer = el;
  }

  onItemClick(index) {
    const { onItemClick, rowClick } = this.props;
    if (!rowClick) {
      this.setState({ selectedIndex: index });
    }
    onItemClick(index);
  }

  fetchMore() {
    const { onFetchMore } = this.props;
    onFetchMore();
  }

  onScroll(event) {
    clearTimeout(this._scrollTimeoutId);

    this._scrollTimeoutId = setTimeout(() => {
      if (event.target.scrollTop + event.target.clientHeight === event.target.scrollHeight) {
        this.fetchMore();
      }
    }, 300);
  }

  renderRow({ index, key, style }) {
    const { divider, data } = this.props;
    return (
      <ListItem
        {...data[index]}
        key={key}
        style={style}
        divider={divider}
        selected={this.state.selectedIndex === index}
        onClick={() => this.onItemClick(index)}
      />
    );
  }

  render() {
    const { className, data, hasMoreButton, maxHeight, fetchOnScroll, divider } = this.props;
    const { rowWidth } = this.state;
    let heightProps = {};
    return (
      <div ref={this.setListContainerRef} className={cn('n2o-widget-list', className)}>
        <div className="n2o-widget-list-container">
          {maxHeight ? (
            <Virtualizer
              width={rowWidth}
              height={maxHeight}
              rowHeight={92}
              rowRenderer={this.renderRow}
              rowCount={data.length}
              overscanRowCount={5}
            />
          ) : (
            map(data, (item, index) => (
              <ListItem
                {...item}
                key={index}
                divider={divider}
                selected={this.state.selectedIndex === index}
                onClick={() => this.onItemClick(index)}
              />
            ))
          )}
        </div>
        {hasMoreButton && !fetchOnScroll && <ListMoreButton onClick={this.fetchMore} />}
      </div>
    );
  }
}

List.propTypes = {
  onItemClick: PropTypes.func,
  autoSelect: PropTypes.bool,
  className: PropTypes.string,
  data: PropTypes.arrayOf(PropTypes.object),
  rowClick: PropTypes.object,
  hasMoreButton: PropTypes.bool,
  onFetchMore: PropTypes.func,
  maxHeight: PropTypes.number,
  fetchOnScroll: PropTypes.bool,
  divider: PropTypes.bool
};
List.defaultProps = {
  onItemClick: () => {},
  onFetchMore: () => {},
  autoSelect: false,
  data: [],
  rowClick: false,
  hasMoreButton: false,
  fetchOnScroll: false,
  divider: true
};

export default List;
