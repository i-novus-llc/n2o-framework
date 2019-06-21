/**
 * Файл конфигурации N2O
 * title - Базовый title для вашего проекта
 * components - список компонентов для сборки
 */
const path = require('path');
module.exports = {
  title: 'N2O',
  application: './AppLayout/AppWithSideBar',
  components: {
    actions: {
      basePath: path.resolve(__dirname, '../src/impl/actions'),
      list: {
        'dummyImpl': './dummy',
        'invoke': './invoke/invoke',
        'hideColumn': './columnVisibility/hideColumn',
        'changeSize': './changeSize/changeSize',
        'showColumn': './columnVisibility/showColumn',
        'showModal': './showModal/showModal',
        'hideWidgetFilters': './changeFiltersVisibility/hideWdgetFilters.js',
        'showWidgetFilters': './changeFiltersVisibility/showWidgetFilters.js',
        'toggleWidgetFilters': './changeFiltersVisibility/toggleWidgetFilters.js',
        'toggleColumn': './columnVisibility/toggleColumn',
        'refreshData': './refreshData/refreshData',
        'exportTable': './exportTable/exportTable',
        'destroyModal': './showModal/destroyModal'
      }
    },
    controls: {
      basePath: path.resolve(__dirname, '../src/components/controls'),
      list: {
        'Input': './Input/Input',
        'Checkbox': './Checkbox/Checkbox',
        'DatePicker': './DatePicker/DatePicker',
        'DateInterval': './DatePicker/DateInterval'
      }
    },
    widgets: {
      basePath: path.resolve(__dirname, '../src/components/widgets'),
      list: {
        'Html': './Html/HtmlWidget',
        'Form': './Form/FormWidget',
        'EditForm': './Form/FormWidget',
        'Table': './Table/TableWidget',
      }
    },
    regions: {
      basePath: path.resolve(__dirname, '../src/components/regions'),
      list: {
        'None': './None/NoneRegion',
        'Tabs': './Tabs/TabsRegion',
        'List': './List/ListRegion',
      }
    },
    layouts: {
      basePath: path.resolve(__dirname, '../src/components/layouts'),
      list: {
        'Single': './Single/Single',
        'TopBottom': './TopBottom/TopBottom',
        'LeftRight': './LeftRight/LeftRight',
        'LeftTopBottom': './LeftTopBottom/LeftTopBottom',
      }
    },
    plugins: {
      basePath: path.resolve(__dirname, '../src/plugins'),
      list: {
        'simple': './simple/simple',
        'SimpleHeaderContainer': './Header/SimpleHeader/SimpleHeaderContainer'
      }
    },
    cells: {
      basePath: path.resolve(__dirname, '../src/components/widgets/Table/cells'),
      list: {
        'TextCell': './TextCell',
        'LinkCell': './LinkCell',
        'IconCell': './IconCell/IconCell',
        'ImageCell': './ImageCell/ImageCell',
        'ProgressBarCell': './ProgressBarCell/ProgressBarCell',
        'BadgeCell': './BadgeCell',
      }
    },
    headers: {
      basePath: path.resolve(__dirname, '../src/components/widgets/Table/headers'),
      list: {
        'TextTableHeader': './TextTableHeader',
      }
    },
    fieldsets: {
      basePath: path.resolve(__dirname, '../src/components/widgets/Form/fieldsets'),
      list: {
        'StandardFieldset': '../Fieldset'
      }
    },
    fields: {
      basePath: path.resolve(__dirname, '../src/components/widgets/Form/fields'),
      list: {
        'StandardField': '../../../controls/Field/StandardField'
      }
    }
  }
};
