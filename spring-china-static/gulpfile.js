var config = require('./config.js');
var spawn = require('child_process').spawn;
var gulp = require('gulp');
var runSequence = require('run-sequence');
var gulpClean = require('gulp-clean');
var gulpPrint = require('gulp-print');
var fs = require('fs');
var mergeStream = require('merge-stream');
var gulpImagemin = require('gulp-imagemin');
var less = require('gulp-less');
var gulpCleanCss = require('gulp-clean-css');
var gulpCssUrlVersioner = require('gulp-css-url-versioner');
var dateFormat = require('date-format');
var gulpUglify = require('gulp-uglify');
var syncExec = require('sync-exec');
var gulpIgnore = require('gulp-ignore');

var nodeModulesCondition = 'node_modules/**/*';

var ENV = {};

ENV.ROOT = process.env.INIT_CWD.replace(/\/$/, '') + '/';
ENV.SRC = './';

ENV.EXT = {
    'static': ['otf', 'eot', 'ttf', 'woff', 'woff2', 'swf', 'svg', 'html', 'xml', 'mp4', 'mp3', 'json', 'md'],
    'image': ['png', 'jpg', 'jpeg', 'gif'],
    'less': ['less'],
    'css': ['css'],
    'js': ['js']
};

ENV.WATCH = {};

var getBuildParam = function (buildGroup, defaultFileBuildPath) {
    var param = [];

    var fileBuildPath = null;
    if (buildGroup in ENV.WATCH) {
        fileBuildPath = ENV.WATCH[buildGroup];
    }
    ENV.WATCH[buildGroup] = null;

    if (!fileBuildPath) {
        var src = null;
        if (typeof defaultFileBuildPath == 'string') {
            src = ENV.SRC + defaultFileBuildPath;
        } else {
            src = [];
            for (var i = 0; i < defaultFileBuildPath.length; i++) {
                src.push(ENV.SRC + defaultFileBuildPath[i]);
            }
        }
        param.push({
            src: src,
            base: ENV.SRC
        });
    } else {
        if (fs.existsSync(ENV.SRC + fileBuildPath)) {
            param.push({
                src: ENV.SRC + fileBuildPath,
                base: ENV.SRC
            });
        }
    }

    return param;
};

var buildWith = function (buildGroup, defaultFileBuildPath, builder) {

    var buildParam = getBuildParam(buildGroup, defaultFileBuildPath);

    var merged = mergeStream();
    for (var i = 0; i < buildParam.length; i++) {
        var build = builder(buildParam[i].src, buildParam[i].base);
        merged.add(build);
    }
    return merged;
};


gulp.task('build:static', function () {
    return buildWith(
        'static',
        '**/*.@(' + ENV.EXT['static'].join('|') + ')',
        function (src, base) {
            return gulp.src(src, {base: base})
                .pipe(gulpIgnore.exclude(nodeModulesCondition))
                .pipe(gulpIgnore.exclude('package.json'))
                .pipe(gulpPrint(function (filepath) {
                    return "build: " + filepath;
                }))
                .pipe(gulp.dest(config.dist));
        }
    );
});

gulp.task('build:image', function () {
    return buildWith(
        'image',
        '**/*.@(' + ENV.EXT['image'].join('|') + ')',
        function (src, base) {
            if (!('imagePack' in config) || config.imagePack) {
                return gulp.src(src, {base: base})
                    .pipe(gulpIgnore.exclude(nodeModulesCondition))
                    .pipe(gulpPrint(function (filepath) {
                        return "build: " + filepath;
                    }))
                    .pipe(gulpImagemin({verbose: true}))
                    .pipe(gulp.dest(config.dist));
            } else {
                return gulp.src(src, {base: base})
                    .pipe(gulpIgnore.exclude(nodeModulesCondition))
                    .pipe(gulpPrint(function (filepath) {
                        return "build: " + filepath;
                    }))
                    .pipe(gulp.dest(config.dist));
            }
        }
    );

});

gulp.task('build:less', function () {
    return buildWith(
        'less',
        '**/*.less',
        function (src, base) {
            return gulp.src(src, {base: base})
                .pipe(gulpIgnore.exclude(nodeModulesCondition))
                .pipe(gulpPrint(function (filepath) {
                    return "build: " + filepath;
                }))
                .pipe(less())
                .on('error', function (err) {
                    console.log(err.message);
                    this.emit('end');
                })
                .pipe(gulpCssUrlVersioner({version: dateFormat.asString('yyMMddhhmmss', new Date())}))
                .pipe(gulpCleanCss({keepSpecialComments: 0}))
                .pipe(gulp.dest(config.dist));
        }
    );
});

gulp.task('build:css', function () {
    return buildWith(
        'css',
        '**/*.css',
        function (src, base) {
            return gulp.src(src, {base: base})
                .pipe(gulpIgnore.exclude(nodeModulesCondition))
                .pipe(gulpPrint(function (filepath) {
                    return "build: " + filepath;
                }))
                .pipe(gulpCssUrlVersioner({version: dateFormat.asString('yyMMddhhmmss', new Date())}))
                .pipe(gulpCleanCss({keepSpecialComments: 0}))
                .pipe(gulp.dest(config.dist));
        }
    );
});

gulp.task('build:js', function () {
    var srcs = [];
    for (var i = 0; i < config.libs.length; i++) {
        srcs.push(config.libs[i] + '/**/*.js');
    }
    return buildWith(
        'js',
        srcs,
        function (src, base) {
            return gulp.src(src, {base: base})
                .pipe(gulpIgnore.exclude(nodeModulesCondition))
                .pipe(gulpPrint(function (filepath) {
                    return "build: " + filepath;
                }))
                .pipe(gulpUglify())
                .on('error', function (err) {
                    console.log(err.message);
                    this.emit('end');
                })
                .pipe(gulp.dest(config.dist));
        }
    );

});

gulp.task('watching', function () {
    var srcs = [];
    for (var i = 0; i < config.libs.length; i++) {
        srcs.push(config.libs[i] + '/**/*.*');
    }
    for (var i = 0; i < config.apps.length; i++) {
        srcs.push(config.apps[i] + '/**/*.*');
    }
    var build = gulp.watch(srcs, function (event) {
        try {

            var path = event.path.substr(ENV.ROOT.length);
            var extension = event.path.substring(event.path.lastIndexOf('.') + 1).toLowerCase();
            var groupFound = null;
            for (var group in ENV.EXT) {
                if (ENV.EXT[group].indexOf(extension) >= 0) {
                    groupFound = group;
                    break;
                }
            }

            if (null != groupFound) {

                if (groupFound == 'js') {
                    for (var i = 0; i < config.apps.length; i++) {
                        if (path.indexOf(config.apps[i] + '/') === 0) {
                            console.log('watcher builder ' + groupFound + ' webpack ignore');
                            return null;
                        }
                    }
                }

                console.log('watcher builder ' + groupFound + ' starting');
                ENV.WATCH[groupFound] = path;
                if ('image' == groupFound) {
                    runSequence(
                        'build:' + groupFound
                        , 'build:less'
                        , 'build:css'
                    );
                } else {
                    runSequence(
                        'build:' + groupFound
                    );
                }
            } else {
                console.log('watcher builder not found for : ' + path);
            }

        } catch (e) {
            console.log('buider error !!!!!');
        }

    });
    return build;
});


gulp.task('default', function () {
    runSequence(
        'build:static'
        , 'build:image'
        , 'build:less'
        , 'build:css'
        , 'build:js'

        , 'watching'
    );
});
