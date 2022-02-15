import React from 'react'
import PropTypes from 'prop-types'

import { ScrollSpyRegionTypes } from './ScrollSpyRegionTypes'
import { Title } from './utils'

export function ContentGroup({ menu, pageId, headlines }) {
    /* headlines && !menu for content in dropdowns */
    return (
        <section className="n2o-scroll-spy-region__content-group">
            {menu.map(({ title, id, menu, content }) => (
                <>
                    <Title
                        title={title}
                        className="n2o-scroll-spy-region__content-title"
                        visible={headlines && !menu}
                    />
                    <Content
                        id={id}
                        content={content}
                        pageId={pageId}
                        menu={menu}
                        headlines={headlines}
                    />
                </>
            ))}
        </section>
    )
}

ContentGroup.propTypes = {
    pageId: PropTypes.string,
    ...ScrollSpyRegionTypes,
}

function Content({ id, content, pageId, menu, headlines }) {
    if (!content && menu) {
        return (
            <ContentGroup
                menu={menu}
                pageId={pageId}
                headlines={headlines}
            />
        )
    }

    /* FIXME remove it and return RegionContent */
    return <div id={id}>Content</div>
    // return <RegionContent content={content} pageId={pageId} />
}

Content.propTypes = {
    pageId: PropTypes.string,
    ...ScrollSpyRegionTypes,
}
