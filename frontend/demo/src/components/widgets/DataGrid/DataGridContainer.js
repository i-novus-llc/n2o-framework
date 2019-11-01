import ReactDataGrid from 'react-data-grid';
import widgetContainer from 'n2o-framework/lib/components/widgets/WidgetContainer';

/**
 * Обертка в widgetContainer, мэппинг пропсов
 */
export default widgetContainer({
  mapProps: props => {
    return {
      columns: props.columns,
      rowGetter: (i) => {
        return props.datasource[i]
      },
      rowsCount: props.datasource && props.datasource.length || 0,
      minHeight: 500
    };
  }
},
'DataGrid'
)(ReactDataGrid);
