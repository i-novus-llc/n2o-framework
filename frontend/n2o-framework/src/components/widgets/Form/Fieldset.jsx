import React from 'react'
import isEqual from 'lodash/isEqual'
import each from 'lodash/each'
import concat from 'lodash/concat'
import { bindActionCreators } from 'redux'
import { connect } from 'react-redux'
import PropTypes from 'prop-types'
import classNames from 'classnames'
import { isNil } from 'lodash'
import isEmpty from 'lodash/isEmpty'

import {
    setMultiFieldVisible,
    setMultiFieldDisabled,
} from '../../../ducks/form/store'
import { WithPropsResolver } from '../../../core/Expression/withResolver'
import { FormContext } from '../../core/FormProvider/provider'

// eslint-disable-next-line import/no-cycle
import FieldsetRow from './FieldsetRow'

/**
 * Компонент - филдсет формы
 * @reactProps {array} rows - ряды, которые содержит филдсет. Они содержат колонки, которые содержат либо поля, либо филдсеты(филдсет рекрсивный).
 * @reactProps {string} className - класс компонента Fieldset
 * @reactProps {string} help - подсказка около label
 * @reactProps {string} labelPosition - позиция лейбела относительно контрола: top-left, top-right, left, right.
 * @reactProps {string} label - заголовок филдсета
 * @reactProps {string} childrenLabel - заголовоки дочерних элементов  филдсета
 * @reactProps {array} labelWidth - ширина лейбела - Либо число, либо 'min' - займет минимальное возможное пространство, либо default - 100px
 * @reactProps {array} labelAlignment - выравнивание текста внутри лейбла
 * @reactProps {number} defaultCol
 * @reactProps {number|string} autoFocusId
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
        super(props)

        this.setVisible = this.setVisible.bind(this)
        this.setEnabled = this.setEnabled.bind(this)
        this.renderRow = this.renderRow.bind(this)

        this.state = { visible: true, enabled: true }

        this.fields = []
        // параметры visible всех полей филдcета
        // необходимо для того, чтобы отличить hidden филдсет (n2o-fieldset empty)
        this.fieldsVisibility = []
    }

    componentDidUpdate() {
        const { visible, enabled, activeModel, propsResolver } = this.props
        const {
            enabled: enabledFromState,
            visible: visibleFromState,
        } = this.state

        const newEnabled = isNil(enabled) || propsResolver(enabled, activeModel)
        const newVisible = isNil(visible) || propsResolver(visible, activeModel)

        if (!isEqual(newEnabled, enabledFromState)) {
            this.setEnabled(newEnabled)
        }

        if (!isEqual(newVisible, visibleFromState)) {
            this.setVisible(newVisible)
        }
    }

    setVisible(nextVisibleField) {
        const { setMultiFieldVisible } = this.props
        const { formName } = this.context

        this.setState(() => {
            setMultiFieldVisible(formName, this.fields, nextVisibleField)

            return {
                visible: nextVisibleField,
            }
        })
    }

    setEnabled(nextEnabledField) {
        const { setMultiFieldDisabled } = this.props
        const { formName } = this.context

        setMultiFieldDisabled(formName, this.fields, !nextEnabledField)
        this.setState({
            enabled: nextEnabledField,
        })
    }

    calculateAllFields(rows) {
        /** @type {{fieldsVisibility: *[], fields: *[]}} */
        const info = { fields: [], fieldsVisibility: [] }

        each(rows, (row) => {
            each(row.cols, (col) => {
                if (col.fieldsets) {
                    each(col.fieldsets, (fieldset) => {
                        const subInfo = this.calculateAllFields(fieldset.rows)

                        info.fields = concat(info.fields, subInfo.fields)
                        info.fieldsVisibility = concat(info.fieldsVisibility, subInfo.fieldsVisibility)
                    })
                } else if (col.fields) {
                    each(col.fields, (field) => {
                        info.fields.push(field.id)
                        info.fieldsVisibility.push(field.visible)
                    })
                }
            })
        })

        return info
    }

    checkGlobalFieldVisibility = fieldsVisibility => fieldsVisibility.some(visible => visible)

    renderRow(rowId, row, props) {
        const {
            labelPosition,
            labelWidth,
            labelAlignment,
            defaultCol,
            autoFocusId,
            modelPrefix,
            autoSubmit,
            activeModel,
            onChange,
            onBlur,
        } = this.props
        const { formName } = this.context
        const { enabled } = this.state

        return (
            <FieldsetRow
                activeModel={activeModel}
                key={rowId}
                row={row}
                rowId={rowId}
                labelPosition={labelPosition}
                labelWidth={labelWidth}
                labelAlignment={labelAlignment}
                defaultCol={defaultCol}
                autoFocusId={autoFocusId}
                form={formName}
                modelPrefix={modelPrefix}
                disabled={!enabled}
                autoSubmit={autoSubmit}
                onChange={onChange}
                onBlur={onBlur}
                {...props}
            />
        )
    }

    render() {
        const {
            className,
            style,
            component: ElementType,
            children,
            parentName,
            label,
            description,
            type,
            childrenLabel,
            activeModel,
            help,
            propsResolver,
            ...rest
        } = this.props

        const { enabled, visible } = this.state

        this.fields = []

        const needLabel = label && type !== 'line'
        const needDescription = description && type !== 'line'

        if (React.Children.count(children)) {
            return <ElementType>{children}</ElementType>
        }

        const classes = classNames('n2o-fieldset', className, {
            'd-none': !visible,
            empty: !isEmpty(this.fieldsVisibility) && !this.checkGlobalFieldVisibility(this.fieldsVisibility),
        })

        const resolvedLabel = propsResolver(label, activeModel)
        const resolvedHelp = propsResolver(help, activeModel)

        return (
            <ElementType
                className={classes}
                style={style}
                needLabel={needLabel}
                needDescription={needDescription}
                childrenLabel={childrenLabel}
                enabled={enabled}
                label={resolvedLabel}
                type={type}
                activeModel={activeModel}
                description={description}
                {...rest}
                render={(rows, props = { parentName }) => {
                    const { fields, fieldsVisibility } = this.calculateAllFields(rows)

                    this.fields = fields
                    this.fieldsVisibility = fieldsVisibility

                    return rows?.map((row, id) => this.renderRow(id, row, props))
                }}
                help={resolvedHelp}
            />
        )
    }
}

Fieldset.propTypes = {
    rows: PropTypes.array,
    className: PropTypes.string,
    label: PropTypes.string,
    childrenLabel: PropTypes.string,
    labelPosition: PropTypes.string,
    description: PropTypes.string,
    labelWidth: PropTypes.array,
    labelAlignment: PropTypes.array,
    defaultCol: PropTypes.number,
    autoFocusId: PropTypes.oneOfType([PropTypes.string, PropTypes.number, PropTypes.oneOf([false])]),
    component: PropTypes.oneOfType([
        PropTypes.string,
        PropTypes.node,
        PropTypes.func,
    ]),
    children: PropTypes.node,
    visible: PropTypes.oneOfType([PropTypes.bool, PropTypes.string]),
    enabled: PropTypes.oneOfType([PropTypes.bool, PropTypes.string]),
    dependency: PropTypes.array,
    setMultiFieldVisible: PropTypes.func,
    setMultiFieldDisabled: PropTypes.func,
    modelPrefix: PropTypes.string,
    type: PropTypes.string,
    parentName: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
    activeModel: PropTypes.object,
    style: PropTypes.object,
    autoSubmit: PropTypes.bool,
    help: PropTypes.string,
    onChange: PropTypes.func,
    onBlur: PropTypes.func,
    propsResolver: PropTypes.func,
}

Fieldset.defaultProps = {
    labelPosition: 'top-left',
    component: 'div',
}

Fieldset.contextType = FormContext

const mapDispatchToProps = dispatch => bindActionCreators(
    {
        setMultiFieldVisible,
        setMultiFieldDisabled,
    },
    dispatch,
)

const Connected = connect(
    null,
    mapDispatchToProps,
)(Fieldset)

export const FieldsetContainer = WithPropsResolver(Connected)
