import React from 'react';
import { connect } from 'react-redux';
import PropTypes from 'prop-types';
import { compose } from 'recompose';
import { makeWidgetPageSelector } from '../../../selectors/widgets';
import { map, forOwn, isEmpty, isEqual } from 'lodash';
import widgetContainer from '../WidgetContainer';
import List from './List';
import withColumn from '../Table/withColumn';
import TableCell from '../Table/TableCell';
import { withWidgetHandlers } from '../Table/TableContainer';
import { createStructuredSelector } from 'reselect';

const ReduxCell = withColumn(TableCell);

class ListContainer extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      needToCombine: false,
      datasource: props.datasource
    };

    this.getWidgetProps = this.getWidgetProps.bind(this);
    this.mapSectionComponents = this.mapSectionComponents.bind(this);
    this.renderCell = this.renderCell.bind(this);
    this.handleItemClick = this.handleItemClick.bind(this);
    this.handleFetchMore = this.handleFetchMore.bind(this);
  }

  componentDidUpdate(prevProps) {
    const { datasource: prevDatasource } = prevProps;
    const { datasource: currentDatasource } = this.props;
    const { needToCombine } = this.state;
    if (currentDatasource && !isEqual(prevDatasource, currentDatasource)) {
      let newDatasource = [];
      if (needToCombine) {
        newDatasource = [...this.state.datasource, ...currentDatasource];
      } else {
        newDatasource = currentDatasource.slice();
      }

      this.setState({
        needToCombine: false,
        datasource: newDatasource
      });
    }
  }

  renderCell(section) {
    if (!section) return;
    return <ReduxCell {...section} className={'n2o-widget-list-cell'} as={'div'} />;
  }

  handleItemClick(index) {
    const { onResolve, datasource, rowClick, onRowClickAction } = this.props;
    onResolve(datasource[index]);
    if (rowClick) {
      onRowClickAction();
    }
  }

  handleFetchMore() {
    const { page, datasource, onFetch } = this.props;
    if (!isEmpty(datasource)) {
      this.setState({ needToCombine: true }, () => onFetch({ page: page + 1 }));
    }
  }

  mapSectionComponents() {
    const { list } = this.props;
    const { datasource } = this.state;
    return map(datasource, item => {
      let mappedSection = {};
      forOwn(item, (v, k) => {
        mappedSection[k] = this.renderCell({ ...list[k], model: item });
      });
      return mappedSection;
    });
  }

  getWidgetProps() {
    const { hasMoreButton, rowClick, isLoading, maxWidth, maxHeight } = this.props;
    return {
      onFetchMore: this.handleFetchMore,
      onItemClick: this.handleItemClick,
      data: this.mapSectionComponents(),
      rowClick,
      hasMoreButton,
      isLoading,
      maxHeight,
      maxWidth
    };
  }
  render() {
    return <List {...this.getWidgetProps()} />;
  }
}

ListContainer.propTypes = {
  datasource: PropTypes.array,
  maxHeight: PropTypes.number,
  maxWidth: PropTypes.number
};

ListContainer.defaultProps = {
  datasource: []
};

const mapStateToProps = createStructuredSelector({
  page: (state, props) => makeWidgetPageSelector(props.widgetId)(state)
});

export default compose(
  widgetContainer(
    {
      mapProps: props => {
        return {
          ...props,
          onActionImpl: props.onActionImpl,
          rowClick: props.rowClick
        };
      }
    },
    'ListWidget'
  ),
  withWidgetHandlers,
  connect(mapStateToProps)
)(ListContainer);
