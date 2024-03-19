import classNames from 'classnames'
import React, { FC, HTMLAttributes } from 'react'

type ViewProps = Pick<HTMLAttributes<HTMLDivElement>, 'onClick' | 'className'>

export const View: FC<ViewProps> = ({ children, className, onClick }) => (
    <div
        className={classNames('n2o-editable-cell-text', className)}
        onClick={onClick}
    >
        {children}
    </div>
)
