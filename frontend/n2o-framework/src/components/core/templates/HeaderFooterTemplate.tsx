import React, { ReactNode } from 'react'

import { SimpleHeader } from '../../../plugins/Header/SimpleHeader/SimpleHeader'
import { Footer } from '../../../plugins/Footer/Footer'
import { ConfigContainer, type ConfigContainerProps } from '../../../plugins/Menu/MenuContainer'

export const HeaderFooterTemplate = ({ children }: { children: ReactNode | ReactNode[] }) => {
    return (
        <div className="application">
            <ConfigContainer render={(config: ConfigContainerProps) => <SimpleHeader {...config} />} />
            <div className="application-body container-fluid">{children}</div>
            <Footer />
        </div>
    )
}

export default HeaderFooterTemplate
