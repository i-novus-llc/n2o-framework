const getResolution = width => {
  let mods = {
    isMobile: false,
    isTablet: false,
    isDesktop: false,
  };

  let resolution = {
    isMobile: width < 639,
    isTablet: width < 1200,
    isDesktop: width > 1200,
  };

  if (resolution.isMobile) {
    mods.isMobile = true;
  } else if (resolution.isTablet) {
    mods.isTablet = true;
  } else {
    mods.isDesktop = true;
  }

  return mods;
};

export default function calcCols(sm, md, lg, width) {
  const { isMobile, isTablet } = getResolution(width);
  const col = isMobile ? sm : isTablet ? md : lg;
  return Math.round(12 / col);
}
