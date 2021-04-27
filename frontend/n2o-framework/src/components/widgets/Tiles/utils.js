const getResolution = (width) => {
    const mods = {
        isMobile: false,
        isTablet: false,
        isDesktop: false,
    }

    const resolution = {
        isMobile: width < 767,
        isTablet: width < 1367,
        isDesktop: width > 1368,
    }

    if (resolution.isMobile) {
        mods.isMobile = true
    } else if (resolution.isTablet) {
        mods.isTablet = true
    } else {
        mods.isDesktop = true
    }

    return mods
}

export default function calcCols(sm, md, lg, width) {
    const { isMobile, isTablet } = getResolution(width)
    const col = isMobile ? sm : isTablet ? md : lg

    return Math.round(12 / col)
}
