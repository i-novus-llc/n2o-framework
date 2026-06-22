import React from 'react'
import { useDispatch, useSelector } from 'react-redux'
import classNames from 'classnames'

import { useTableWidget } from '../../widgets/AdvancedTable'
import { FactoryStandardButton, type Props } from '../FactoryStandardButton'
import { dataSourceByIdSelector, dataSourceSaveSettings } from '../../../ducks/datasource/selectors'
import { dataRequest, register } from '../../../ducks/datasource/store'
import { useData } from '../../../core/widget/useData'
import type { DataSourceCache } from '../../../ducks/datasource/DataSource'

export function ResetSettings({ className, datasource, ...rest }: Props) {
    const { resetSettings } = useTableWidget()
    const { setValue } = useData<DataSourceCache>(datasource || '')

    const dispatch = useDispatch()

    const dataSourceSavedSettings = useSelector(dataSourceSaveSettings(datasource || ''))
    const reduxDatasourceProps = useSelector(dataSourceByIdSelector(datasource || ''))

    const onClick = () => {
        // Сброс для table
        resetSettings()

        // Сброс для datasource
        if (datasource && dataSourceSavedSettings) {
            const { defaultDatasourceProps = {} } = reduxDatasourceProps
            const { paging, sorting = {} } = defaultDatasourceProps

            const { page = 1, size = 1 } = paging || {}

            const config = { ...reduxDatasourceProps, sorting, paging: { ...reduxDatasourceProps.paging, page, size } }

            setValue?.(null)

            dispatch(register(datasource, config))
        }
    }

    return <FactoryStandardButton {...rest} className={classNames('reset-settings-btn', className)} onClick={onClick} />
}
