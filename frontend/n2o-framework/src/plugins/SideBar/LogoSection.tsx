import classNames from 'classnames'
import React from 'react'

import { Logo } from '../Header/SimpleHeader/Logo'

import { LogoSectionProps } from './types'

export const LogoSection = ({
    isMiniView,
    logo,
    subtitle,
    showContent,
}: LogoSectionProps) => (
    <div className={classNames(
        'n2o-sidebar__nav-brand n2o-nav-brand',
        {
            'justify-content-center': isMiniView,
        },
    )}
    >
        <div className="d-flex align-items-center">
            {logo && <Logo {...logo} subtitle={subtitle} showContent={showContent} isMiniView={isMiniView} />}
        </div>
    </div>
)
