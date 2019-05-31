import React from 'react';
import { isBoolean, isString, each, concat } from 'lodash';
import { bindActionCreators } from 'redux';
import { compose } from 'recompose';
import { connect } from 'react-redux';
import PropTypes from 'prop-types';
import cx from 'classnames';
import {
  showFields,
  hideFields,
  enableFields,
  disableFields,
} from '../../../actions/formPlugin';
import propsResolver from '../../../utils/propsResolver';
import withObserveDependency from '../../../core/dependencies/withObserveDependency';
import { makeGetResolveModelSelector } from '../../../selectors/models';
import FieldsetRow from './FieldsetRow';

const config = {
  onChange: function() {
    const { store } = this.context;
    const { visible, enabled } = this.props;
    if (isString(visible) || isString(enabled)) {
      const formValues = this.getFormValues(store);
      visible && this.setVisible(propsResolver(visible, formValues));
      enabled && this.setEnabled(propsResolver(enabled, formValues));
    }
  },
};

/**
 * Компонент - филдсет формы
 * @reactProps {array} rows - ряды, которые содержит филдсет. Они содержат колонки, которые содержат либо поля, либо филдсеты(филдсет рекрсивный).
 * @reactProps {string} className - класс компонента Fieldset
 * @reactProps {string} labelPosition - позиция лейбела относительно контрола: top-left, top-right, left, right.
 * @reactProps {array} labelWidth - ширина лейбела - Либо число, либо 'min' - займет минимальное возможное пространство, либо default - 100px
 * @reactProps {array} labelAlignment - выравнивание текста внутри лейбла
 * @reactProps {number} defaultCol
 * @reactProps {number} autoFocusId
 * @reactProps {node} component
 * @reactProps {node} children
 * @example
 *
 * //пример структуры rows
 * const rows = [
 *    {
 *      "cols": [
 *        {
 *          "fields": [
 *            {
 *            //...
 *            }
 *          ]
 *        },
 *        {
 *          "fields": [
 *            {
 *            //...
 *            }
 *          ]
 *        },
 *        {
 *          "fields": [
 *            {
 *            //...
 *            }
 *          ]
 *        },
 *      ]
 *    },
 *    {
 *      "cols": [
 *        {
 *          "fieldsets": [
 *            {
 *            "rows": [
 *            //...
 *            ]
 *            }
 *          ]
 *        },
 *        {
 *          "fields": [
 *            {
 *            //...
 *            },
 *            {
 *            //...
 *            }
 *          ]
 *        },
 *      ]
 *    }
 *  ]
 *
 *  <Fieldset rows={rows}>
 *
 */

class Fieldset extends React.Component {
  constructor(props) {
    super(props);

    this.setVisible = this.setVisible.bind(this);
    this.setEnabled = this.setEnabled.bind(this);
    this.getFormValues = this.getFormValues.bind(this);
    this.renderRow = this.renderRow.bind(this);

    this.state = {
      visibleFieldset: true,
    };

    this.fields = [];
  }

  static getDerivedStateFromProps(props, state) {
    if (props.visible !== state.visibleFieldset && isBoolean(props.visible)) {
      return {
        visibleFieldset: props.visible,
      };
    }
    return null;
  }

  setVisible(nextVisibleField) {
    const { showFields, hideFields, form } = this.props;
    this.setState(() => {
      if (nextVisibleField) {
        showFields(form, this.fields);
      } else {
        hideFields(form, this.fields);
      }
      return { visibleFieldset: !!nextVisibleField };
    });
  }

  setEnabled(nextEnabledField) {
    const { enableFields, disableFields, form } = this.props;
    if (nextEnabledField) {
      enableFields(form, this.fields);
    } else {
      disableFields(form, this.fields);
    }
  }

  getFormValues(store) {
    const state = store.getState();
    return makeGetResolveModelSelector(this.props.form)(state);
  }

  calculateAllFields(rows) {
    let fields = [];
    each(rows, row => {
      each(row.cols, col => {
        if (col.fieldsets) {
          each(col.fieldsets, fieldset => {
            fields = concat(fields, this.calculateAllFields(fieldset.rows));
          });
        } else if (col.fields) {
          each(col.fields, field => {
            fields.push(field.id);
          });
        }
      });
    });
    return fields;
  }

  renderRow(rowId, row) {
    const {
      labelPosition,
      labelWidth,
      labelAlignment,
      defaultCol,
      autoFocusId,
      form,
      modelPrefix,
    } = this.props;
    return (
      <FieldsetRow
        row={row}
        rowId={rowId}
        labelPosition={labelPosition}
        labelWidth={labelWidth}
        labelAlignment={labelAlignment}
        defaultCol={defaultCol}
        autoFocusId={autoFocusId}
        form={form}
        modelPrefix={modelPrefix}
      />
    );
  }

  render() {
    const {
      className,
      style,
      component: ElementType,
      children,
      ...rest
    } = this.props;
    this.fields = [];
    if (React.Children.count(children)) {
      return <ElementType>{children}</ElementType>;
    }

    const classes = cx('n2o-fieldset', className, {
      'd-none': !this.state.visibleFieldset,
    });

    return (
      <div className={classes} style={style}>
        <ElementType
          {...rest}
          render={rows => {
            this.fields = this.calculateAllFields(rows);
            return rows.map((row, id) => this.renderRow(id, row));
          }}
        />
      </div>
    );
  }
}

Fieldset.propTypes = {
  rows: PropTypes.array,
  className: PropTypes.string,
  labelPosition: PropTypes.string,
  labelWidth: PropTypes.array,
  labelAlignment: PropTypes.array,
  defaultCol: PropTypes.number,
  autoFocusId: PropTypes.number,
  component: PropTypes.node,
  children: PropTypes.node,
  visible: PropTypes.oneOfType([PropTypes.bool, PropTypes.string]),
  enabled: PropTypes.oneOfType([PropTypes.bool, PropTypes.string]),
  dependency: PropTypes.array,
  form: PropTypes.string,
  showFields: PropTypes.func,
  hideFields: PropTypes.func,
  enableFields: PropTypes.func,
  disableFields: PropTypes.func,
  modelPrefix: PropTypes.string,
};

Fieldset.defaultProps = {
  labelPosition: 'top-left',
  component: 'div',
};

Fieldset.contextTypes = {
  store: PropTypes.object,
};

const mapDispatchToProps = dispatch =>
  bindActionCreators(
    {
      showFields,
      hideFields,
      enableFields,
      disableFields,
    },
    dispatch
  );

const FieldsetContainer = compose(
  connect(
    null,
    mapDispatchToProps
  ),
  withObserveDependency(config)
)(Fieldset);

export default FieldsetContainer;
