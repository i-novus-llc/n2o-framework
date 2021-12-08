import React from 'react'
import PropTypes from 'prop-types'
import { compose } from 'recompose'
import { withTranslation } from 'react-i18next'
import map from 'lodash/map'
import forOwn from 'lodash/forOwn'
import isEmpty from 'lodash/isEmpty'
import isEqual from 'lodash/isEqual'
import find from 'lodash/find'

import { modelsType } from '../../../core/widget/propTypes'
import withColumn from '../Table/withColumn'
import TableCell from '../Table/TableCell'
import { withWidgetHandlers } from '../AdvancedTable/AdvancedTableContainer'

// eslint-disable-next-line import/no-named-as-default
import List from './List'

const ReduxCell = withColumn(TableCell)

/**
 * Контейнер виджета ListWidget
 * @reactProps {object} list - объект конфиг секций в виджете
 * @reactProps {object|null} rowClick - кастомный клик
 * @reactProps {boolean} hasMoreButton - флаг включения загрузки по нажатию на кнопку
 * @reactProps {number} maxHeight - максимальная высота виджета
 * @reactProps {boolean} fetchOnScroll - запрос при скролле
 * @reactProps {boolean} hasSelect - флаг выбора строк
 */
class ListContainer extends React.Component {
    constructor(props) {
        super(props)

        const { models } = props
        const { datasource } = models

        this.state = {
            needToCombine: false,
            datasource,
        }

        this.getWidgetProps = this.getWidgetProps.bind(this)
        this.mapSectionComponents = this.mapSectionComponents.bind(this)
        this.renderCell = this.renderCell.bind(this)
        this.handleItemClick = this.handleItemClick.bind(this)
        this.handleFetchMore = this.handleFetchMore.bind(this)
    }

    componentDidUpdate({ models: prevModels }) {
        const { datasource: prevDatasource } = prevModels
        const { models, setResolve } = this.props
        const { datasource: currentDatasource, resolve } = models
        const selectedId = resolve?.id
        const { needToCombine } = this.state

        if (currentDatasource && !isEqual(prevDatasource, currentDatasource)) {
            // noinspection JSUnusedAssignment
            let newDatasource = []
            const { datasource } = this.state

            if (needToCombine) {
                newDatasource = [...datasource, ...currentDatasource]
            } else {
                newDatasource = currentDatasource.slice()
            }

            this.setState(
                {
                    needToCombine: false,
                    datasource: newDatasource,
                },
                () => {
                    const model = selectedId
                        ? find(currentDatasource, item => item.id === selectedId)
                        : currentDatasource[0]

                    if (model) { setResolve(model) }
                },
            )
        }
    }

    renderCell(section) {
        const { id } = this.props

        if (!section) { return }

        // eslint-disable-next-line consistent-return
        return (
            <ReduxCell
                {...section}
                widgetId={id}
                positionFixed={false}
                modifiers={{}}
            />
        )
    }

    handleItemClick(index) {
        const { setResolve, models, rowClick, onRowClickAction } = this.props
        const { datasource } = models

        setResolve(datasource[index])
        if (rowClick) {
            onRowClickAction()
        }
    }

    handleFetchMore() {
        const { page, models, setPage } = this.props
        const { datasource } = models

        if (!isEmpty(datasource)) {
            this.setState({ needToCombine: true }, () => setPage(page + 1))
        }
    }

    mapSectionComponents() {
        const { list } = this.props
        const { datasource } = this.state

        return map(datasource, (item) => {
            const mappedSection = {}

            forOwn(list, (v, k) => {
                mappedSection[k] = this.renderCell({
                    ...list[k],
                    id: v.id,
                    model: item,
                })
            })

            return mappedSection
        })
    }

    getWidgetProps() {
        const {
            hasMoreButton,
            rowClick,
            placeholder,
            maxHeight,
            fetchOnScroll,
            divider,
            hasSelect,
            models,
            rows,
            t,
        } = this.props

        return {
            onFetchMore: this.handleFetchMore,
            onItemClick: this.handleItemClick,
            data: this.mapSectionComponents(),
            rowClick,
            hasMoreButton,
            placeholder,
            maxHeight,
            fetchOnScroll,
            divider,
            hasSelect,
            selectedId: models.resolve?.id,
            rows,
            t,
        }
    }

    render() {
        return <List {...this.getWidgetProps()} />
    }
}

ListContainer.propTypes = {
    id: PropTypes.string,
    toolbar: PropTypes.object,
    disabled: PropTypes.bool,
    pageId: PropTypes.string,
    className: PropTypes.string,
    style: PropTypes.object,
    filter: PropTypes.object,
    dataProvider: PropTypes.object,
    fetchOnInit: PropTypes.bool,
    list: PropTypes.object,
    fetchOnScroll: PropTypes.bool,
    rowClick: PropTypes.func,
    hasMoreButton: PropTypes.bool,
    maxHeight: PropTypes.number,
    models: PropTypes.shape(modelsType),
    hasSelect: PropTypes.bool,
    setResolve: PropTypes.func,
    onRowClickAction: PropTypes.func,
    page: PropTypes.func,
    setPage: PropTypes.func,
    placeholder: PropTypes.string,
    divider: PropTypes.bool,
    rows: PropTypes.object,
    t: PropTypes.func,
}

ListContainer.defaultProps = {
    rowClick: null,
    hasMoreButton: false,
    toolbar: {},
    disabled: false,
    className: '',
    style: {},
    filter: {},
    list: {},
    fetchOnScroll: false,
    hasSelect: false,
}

export default compose(
    withTranslation(),
    withWidgetHandlers,
)(ListContainer)
