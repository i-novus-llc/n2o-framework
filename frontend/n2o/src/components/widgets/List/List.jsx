import React, { Component } from 'react';
import cn from 'classnames';
import { map } from 'lodash';
import PropTypes from 'prop-types';
import ListItem from './ListItem';
import ListMoreButton from './ListMoreButton';

class List extends Component {
  constructor(props) {
    super(props);

    this.state = {
      selectedIndex: props.selectedId || (props.autoSelect ? 0 : null)
    };

    this._scrollTimeoutId = null;

    this.onItemClick = this.onItemClick.bind(this);
    this.fetchMore = this.fetchMore.bind(this);
    this.onScroll = this.onScroll.bind(this);
    this.setListContainerRef = this.setListContainerRef.bind(this);
  }

  componentDidMount() {
    this._listContainer.addEventListener('scroll', this.onScroll, true);
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

  render() {
    const { className, data, hasMoreButton, maxHeight, fetchOnScroll } = this.props;
    return (
      <div
        ref={this.setListContainerRef}
        className={cn('n2o-widget-list', className)}
        style={{
          maxHeight: maxHeight
        }}
      >
        <div className="n2o-widget-list-container">
          {map(data, (item, index) => (
            <ListItem
              {...item}
              selected={this.state.selectedIndex === index}
              onClick={() => this.onItemClick(index)}
            />
          ))}
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
  fetchOnScroll: PropTypes.bool
};
List.defaultProps = {
  onItemClick: () => {},
  onFetchMore: () => {},
  autoSelect: false,
  data: [],
  rowClick: false,
  hasMoreButton: false,
  fetchOnScroll: false
};

export default List;
