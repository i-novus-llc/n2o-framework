import React, { Component } from 'react';
import cn from 'classnames';
import { map, isEqual } from 'lodash';
import PropTypes from 'prop-types';
import ListItem from './ListItem';
import ListMoreButton from './ListMoreButton';
import { WindowScroller, AutoSizer, List as Virtualizer } from 'react-virtualized';

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
    this._setVirtualizerRef = this._setVirtualizerRef.bind(this);
    this._setWindowScrollerRef = this._setWindowScrollerRef.bind(this);
  }

  componentDidMount() {
    const { fetchOnScroll } = this.props;
    if (fetchOnScroll) {
      this._listContainer.addEventListener('scroll', this.onScroll, true);
    }
    this.setState({
      rowWidth: this._listContainer.clientWidth
    });
  }

  componentDidUpdate(prevProps) {
    const { data, hasMoreButton, fetchOnScroll, maxHeight } = this.props;
    if (hasMoreButton && !fetchOnScroll && !isEqual(prevProps.data, data)) {
      if (maxHeight) {
        this._virtualizer.scrollToRow(data.length);
      } else {
        this._windowScroller.updatePosition();
      }
    }
  }

  componentWillUnmount() {
    const { fetchOnScroll } = this.props;
    if (fetchOnScroll) {
      this._listContainer.removeEventListener('scroll', this.onScroll);
    }
  }

  setListContainerRef(el) {
    this._listContainer = el;
  }

  _setWindowScrollerRef(el) {
    this._windowScroller = el;
  }

  _setVirtualizerRef(el) {
    this._virtualizer = el;
  }

  onItemClick(index) {
    const { onItemClick, rowClick } = this.props;
    if (!rowClick) {
      this.setState({ selectedIndex: index }, () => {
        if (this._virtualizer) {
          this._virtualizer.forceUpdateGrid();
        }
      });
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
    const { divider, data, hasMoreButton, fetchOnScroll } = this.props;
    if (index === data.length - 1 && hasMoreButton && !fetchOnScroll) {
      return <ListMoreButton style={style} onClick={this.fetchMore} />;
    }
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
    const { className, data, maxHeight } = this.props;
    const { rowWidth } = this.state;
    return (
      <div ref={this.setListContainerRef} className={cn('n2o-widget-list', className)}>
        <div className="n2o-widget-list-container">
          {maxHeight ? (
            <Virtualizer
              ref={this._setVirtualizerRef}
              width={rowWidth}
              height={maxHeight}
              rowHeight={92}
              rowRenderer={this.renderRow}
              rowCount={data.length}
              overscanRowCount={5}
            />
          ) : (
            <WindowScroller ref={this._setWindowScrollerRef} scrollElement={window}>
              {({ height, isScrolling, registerChild, onChildScroll, scrollTop }) => (
                <Virtualizer
                  ref={this._setVirtualizerRef}
                  autoHeight
                  height={height}
                  isScrolling={isScrolling}
                  onScroll={onChildScroll}
                  overscanRowCount={5}
                  rowCount={data.length}
                  rowHeight={92}
                  rowRenderer={this.renderRow}
                  scrollTop={scrollTop}
                  width={rowWidth}
                />
              )}
            </WindowScroller>
          )}
        </div>
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
