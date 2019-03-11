import React from 'react';
import { lifecycle, compose, withHandlers } from 'recompose';
import { isEqual, find, isEmpty, debounce } from 'lodash';

import Tree from '../component/Tree';
import widgetContainer from '../../WidgetContainer';
import { setTableSelectedId } from '../../../../actions/widgets';
import { TREE } from '../../widgetTypes';

export const withWidgetContainer = widgetContainer(
  {
    mapProps: props => {
      return {
        widgetId: props.widgetId,
        pageId: props.pageId,
        isActive: props.isActive,
        hasFocus: props.hasFocus,
        hasSelect: props.hasSelect,
        autoFocus: props.autoFocus,
        datasource: props.datasource,
        resolveModel: props.resolveModel,
        onResolve: debounce(newModel => {
          props.onResolve(newModel);
          if (props.selectedId != newModel.id) {
            props.dispatch(setTableSelectedId(props.widgetId, newModel.id));
          }
        }, 100),
        onFocus: props.onFocus,
        size: props.size,
        actions: props.actions,
        redux: true,
        onActionImpl: props.onActionImpl,
        rowClick: props.rowClick,

        childIcon: props.childIcon,
        multiselect: props.multiselect,
        showLine: props.showLine,
        filter: props.filter,
        expandBtn: props.expandBtn,
        bulkData: props.bulkData,
        parentFieldId: props.parentFieldId,
        valueFieldId: props.valueFieldId,
        labelFieldId: props.labelFieldId,
        iconFieldId: props.iconFieldId,
        imageFieldId: props.imageFieldId,
        badgeFieldId: props.badgeFieldId,
        badgeColorFieldId: props.badgeColorFieldId,
        hasCheckboxes: props.hasCheckboxes,
        draggable: props.draggable,
        childrenFieldId: props.childrenFieldId
      };
    }
  },
  TREE
);

// export const withContainerLiveCycle = lifecycle({
//   componentWillReceiveProps(nextProps) {
//     const { selectedId: prevSelectedId, datasource: prevDatasource, onResolve } = this.props;
//     const { hasSelect, datasource, selectedId } = nextProps;
//
//     if (
//       hasSelect &&
//       !isEmpty(datasource) &&
//       !isEqual(prevDatasource, datasource) &&
//       (!selectedId ||
//         !isEqual(prevSelectedId, selectedId) ||
//         !isEqualCollectionItemsById(prevDatasource, datasource, selectedId))
//     ) {
//       const selectedModel = find(datasource, model => model.id == selectedId);
//       const resolveModel = selectedModel || datasource[0];
//       onResolve(resolveModel);
//     }
//   }
// });
//
export const withWidgetHandlers = withHandlers({
  onRowClickAction: ({ rowClick, onActionImpl }) => () => {
    onActionImpl(rowClick);
  }
});

export default compose(
  withWidgetContainer,
  withWidgetHandlers
)(Tree);
