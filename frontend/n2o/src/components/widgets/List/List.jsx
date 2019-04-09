import React, { Component } from 'react';
import cn from 'classnames';
import ReactDom from 'react-dom';
import { map, isEqual, isEmpty } from 'lodash';
import PropTypes from 'prop-types';
import ListItem from './ListItem';
import ListMoreButton from './ListMoreButton';
import {
  WindowScroller,
  AutoSizer,
  CellMeasurer,
  CellMeasurerCache,
  List as Virtualizer,
} from 'react-virtualized';
import { getIndex } from '../Table/Table';

const SCROLL_OFFSET = 100;

/**
 * Компонент List
 * @reactProps {number|string} selectedId - id выбранной записи
 * @reactProps {boolean} autoFocus - фокус при инициализации на перой или на selectedId строке
 * @reactProps {function} onItemClick - callback при клике на строку
 * @reactProps {object} rowClick - кастомное действие клика
 * @reactProps {function} onFetchMore - callback при клика на "Загрузить еще" или скролле
 * @reactProps {string|number} selectedId - id выбранной записи
 */
class List extends Component {
  constructor(props) {
    super(props);
    this.state = {
      selectedIndex: props.hasSelect
        ? getIndex(props.data, props.selectedId)
        : null,
      data: props.data,
    };
    this.cache = new CellMeasurerCache({
      fixedWidth: true,
      defaultHeight: 90,
    });

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
  }

  componentDidUpdate(prevProps) {
    const {
      data,
      hasMoreButton,
      fetchOnScroll,
      maxHeight,
      selectedId,
    } = this.props;
    if (!isEqual(prevProps, this.props)) {
      let state = {};
      if (hasMoreButton && !fetchOnScroll && !isEqual(prevProps.data, data)) {
        if (maxHeight) {
          this._virtualizer.scrollToRow(data.length);
        } else {
          const scrollHeight = ReactDom.findDOMNode(this._virtualizer)
            .scrollHeight;
          window.scrollTo(0, scrollHeight);
        }
      }

      if (!isEqual(prevProps.data, data)) {
        state = {
          ...state,
          data: hasMoreButton ? [...data, {}] : data,
        };
      }

      if (selectedId && !isEqual(prevProps.selectedId, selectedId)) {
        state = {
          ...state,
          selectedIndex: getIndex(data, selectedId),
        };
      }

      this.setState(state);
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
    const { onItemClick, rowClick, hasSelect } = this.props;
    if (!rowClick && hasSelect) {
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
      const scrollPosition = event.target.scrollTop + event.target.clientHeight;
      const minScrollToLoad = event.target.scrollHeight - SCROLL_OFFSET;
      if (
        scrollPosition >= minScrollToLoad ||
        scrollPosition === event.target.scrollHeight
      ) {
        this.fetchMore();
      }
    }, 300);
  }

  renderRow({ index, key, style, parent }) {
    const { divider, hasMoreButton, fetchOnScroll } = this.props;
    const { data } = this.state;
    let moreBtn = null;
    if (index === data.length - 1 && hasMoreButton && !fetchOnScroll) {
      return (
        <CellMeasurer
          key={key}
          cache={this.cache}
          parent={parent}
          columnIndex={0}
          rowIndex={index}
        >
          <ListMoreButton style={style} onClick={this.fetchMore} />
        </CellMeasurer>
      );
    }

    return (
      <React.Fragment>
        <CellMeasurer
          key={key}
          cache={this.cache}
          parent={parent}
          columnIndex={0}
          rowIndex={index}
        >
          <ListItem
            {...data[index]}
            key={key}
            style={style}
            divider={divider}
            selected={this.state.selectedIndex === index}
            onClick={() => this.onItemClick(index)}
          />
        </CellMeasurer>
        {moreBtn}
      </React.Fragment>
    );
  }

  render() {
    const { className, maxHeight } = this.props;
    const { data } = this.state;
    return (
      <div
        ref={this.setListContainerRef}
        className={cn('n2o-widget-list', className)}
      >
        {(!data || isEmpty(data)) && (
          <div className="n2o-widget-list--empty-view text-muted">
            Нет данных для отображения
          </div>
        )}
        <div className="n2o-widget-list-container">
          {maxHeight ? (
            <AutoSizer style={{ height: '100%' }}>
              {({ width }) => (
                <Virtualizer
                  ref={this._setVirtualizerRef}
                  width={width}
                  height={maxHeight}
                  deferredMeasurementCache={this.cache}
                  rowHeight={this.cache.rowHeight}
                  rowRenderer={this.renderRow}
                  rowCount={data.length}
                  overscanRowCount={5}
                />
              )}
            </AutoSizer>
          ) : (
            <WindowScroller
              ref={this._setWindowScrollerRef}
              scrollElement={window}
            >
              {({
                height,
                isScrolling,
                registerChild,
                onChildScroll,
                scrollTop,
              }) => (
                <AutoSizer style={{ height: '100%' }}>
                  {({ width }) => (
                    <Virtualizer
                      ref={this._setVirtualizerRef}
                      autoHeight
                      height={height}
                      isScrolling={isScrolling}
                      onScroll={onChildScroll}
                      overscanRowCount={5}
                      rowCount={data.length}
                      rowHeight={this.cache.rowHeight}
                      rowRenderer={this.renderRow}
                      scrollTop={scrollTop}
                      width={width}
                    />
                  )}
                </AutoSizer>
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
  hasSelect: PropTypes.bool,
  className: PropTypes.string,
  data: PropTypes.arrayOf(PropTypes.object),
  rowClick: PropTypes.object,
  hasMoreButton: PropTypes.bool,
  onFetchMore: PropTypes.func,
  maxHeight: PropTypes.number,
  fetchOnScroll: PropTypes.bool,
  divider: PropTypes.bool,
  selectedId: PropTypes.oneOf(PropTypes.string, PropTypes.number),
};
List.defaultProps = {
  onItemClick: () => {},
  onFetchMore: () => {},
  hasSelect: false,
  data: [],
  rowClick: false,
  hasMoreButton: false,
  fetchOnScroll: false,
  divider: true,
};

export default List;
