import { connect } from 'react-redux';
import { lifecycle, compose } from 'recompose';
import { isEqual, find, isEmpty, debounce } from 'lodash';

import AdvancedTable from './AdvancedTable';
import widgetContainer from '../WidgetContainer';
import { setTableSelectedId } from '../../../actions/widgets';
import { ADVANCED_TABLE } from '../widgetTypes';

const isEqualCollectionItemsById = (data1 = [], data2 = [], selectedId) => {
  const predicate = ({ id }) => id == selectedId;
  return isEqual(find(data1, predicate), find(data2, predicate));
};

export default compose(
  widgetContainer(
    {
      mapProps: props => {
        return {
          widgetId: props.widgetId,
          pageId: props.pageId,
          headers: props.headers,
          cells: props.cells,
          isAnyTableFocused: props.isAnyTableFocused,
          isActive: props.isActive,
          hasFocus: props.hasFocus,
          hasSelect: props.hasSelect,
          autoFocus: props.autoFocus,
          datasource: props.datasource,
          selectedId: props.selectedId,
          sorting: props.sorting,
          rowColor: props.rowColor,
          onSort: props.onSort,
          onResolve: debounce(newModel => {
            props.onResolve(newModel);
            if (props.selectedId != newModel.id) {
              props.dispatch(setTableSelectedId(props.widgetId, newModel.id));
            }
          }, 100),
          onFocus: props.onFocus,
          size: props.size,
          actions: props.actions,
          redux: true
        };
      }
    },
    ADVANCED_TABLE
  ),
  lifecycle({
    componentWillReceiveProps(nextProps) {
      const { selectedId: prevSelectedId, datasource: prevDatasource, onResolve } = this.props;
      const { hasSelect, datasource, selectedId } = nextProps;

      if (
        hasSelect &&
        !isEmpty(datasource) &&
        !isEqual(prevDatasource, datasource) &&
        (!selectedId ||
          !isEqual(prevSelectedId, selectedId) ||
          !isEqualCollectionItemsById(prevDatasource, datasource, selectedId))
      ) {
        const selectedModel = find(datasource, model => model.id == selectedId);
        const resolveModel = selectedModel || datasource[0];
        onResolve(resolveModel);
      }
    }
  })
)(AdvancedTable);
