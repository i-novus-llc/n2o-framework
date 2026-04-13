import React, { ComponentType } from 'react'

export const WithComponentId = (defaultId: string) => {
    return <P extends object>(Component: ComponentType<P>) => {
        return (props: P & { id?: string }) => {
            return <Component {...props} id={props.id || defaultId} />
        }
    }
}
