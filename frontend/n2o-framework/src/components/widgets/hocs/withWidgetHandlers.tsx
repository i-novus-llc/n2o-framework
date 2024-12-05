import React, { ComponentType } from 'react'

import { type ClickAction, useOnActionMethod } from '../hooks/useOnActionMethod'

export const withWidgetHandlers = <P extends object>(WrappedComponent: ComponentType<P>) => {
    return (props: P & { rowClick: ClickAction, datasource: string }) => {
        const { rowClick, datasource } = props
        const onRowClickAction = useOnActionMethod(datasource, rowClick)

        return <WrappedComponent {...props} onRowClickAction={onRowClickAction} />
    }
}
