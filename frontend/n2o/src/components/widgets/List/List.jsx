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

    this.onItemClick = this.onItemClick.bind(this);
    this.fetchMore = this.fetchMore.bind(this);
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

  render() {
    const { className, data, hasMoreButton, isLoading, maxWidth, maxHeight } = this.props;
    return (
      <div
        className={cn('n2o-widget-list', className)}
        style={{
          maxWidth: maxWidth,
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
        {hasMoreButton && <ListMoreButton onClick={this.fetchMore} isLoading={isLoading} />}
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
  maxWidth: PropTypes.number,
  maxHeight: PropTypes.number
};
List.defaultProps = {
  onItemClick: () => {},
  onFetchMore: () => {},
  autoSelect: false,
  data: [],
  rowClick: false,
  hasMoreButton: false
};

export default List;
