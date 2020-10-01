package com.scorpion.NeonphotoEditor;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.bumptech.glide.signature.ObjectKey;
import com.scorpion.NeonphotoEditor.Adapters.FilterAdapter;
import com.scorpion.NeonphotoEditor.Adapters.FontAdapter;
import com.scorpion.NeonphotoEditor.Adapters.ImageSpiralAdapter;
import com.scorpion.NeonphotoEditor.autocutimage.ImageBlurMaker;
import com.scorpion.NeonphotoEditor.autocutimage.ImageCutter;
import com.scorpion.NeonphotoEditor.autocutimage.ImageUtils;
import com.scorpion.NeonphotoEditor.Colorfiles.AmbilWarnaDialog;
import com.scorpion.NeonphotoEditor.Multitouch.MultiTouchListener;
import com.scorpion.NeonphotoEditor.Util.Constant;
import com.scorpion.NeonphotoEditor.Util.Helper;
import com.scorpion.NeonphotoEditor.Util.Path;
import com.scorpion.NeonphotoEditor.Util.SetLayparam;
import com.zomato.photofilters.SampleFilters;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.imageprocessors.subfilters.BrightnessSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.ColorOverlaySubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.ContrastSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.SaturationSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.VignetteSubFilter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import jp.wasabeef.blurry.Blurry;

public class PhotoEffectEditor extends Activity implements FilterAdapter.OnFilterListener {

    final int BACKGROUND = 2;
    final int CLOSE_ALL = -1;
    final int FILTERS = 3;
    final int FILTER_COUNT = 11;
    final int GRADIENT = 23;
    final int REQUEST_GALLERY = 101;
    final int REQUEST_TEXTURE = 102;
    Bitmap RedCutBit;
    final int SPIRAL = 0;
    final int TEXT = 1;
    ImageSpiralAdapter adapter;
    ArrayList<Bitmap> albitmap = new ArrayList<>();
    ArrayList<PointF> allredpath = new ArrayList<>();
    Bitmap backBitmap;
    int bgcolor = ViewCompat.MEASURED_STATE_MASK;
    int celement;
    Context context;
    Bitmap cropBitmap;
    AsyncTask<Bitmap, Integer, Bitmap> croptask;
    EditText ettext;
    Typeface externalFont;
    FilterAdapter fadapter;
    int[] gcolor;
    TextView header;
    int height;
    HorizontalScrollView hsvbg;
    ImageCutter imageCutter;
    String imgPath;
    boolean isDoubleClick = false;
    ImageView ivbg;
    ImageView ivbgcolor;
    ImageView ivblur;
    ImageView ivbspiral;
    ImageView ivcolor;
    ImageView ivcut;
    ImageView ivdone;
    ImageView ivfilter;
    ImageView ivfirst;
    ImageView ivfont;
    ImageView ivfspiral;
    ImageView ivgallery;
    ImageView ivgback;
    ImageView ivgradient;
    ImageView ivoriginal;
    ImageView ivsecond;
    ImageView ivspiral;
    ImageView ivtdone;
    ImageView ivtext;
    ImageView ivtexture;
    LinearLayout lbg;
    LinearLayout lbottom;
    LinearLayout lgradient;
    LinearLayout ltext;
    Bitmap mainBitmap;
    RecyclerView rcvfilter;
    RecyclerView rcvspiral;
    RelativeLayout relTextLayout;
    RelativeLayout reledit;
    RelativeLayout releditor;
    RelativeLayout relpbar;
    int textcolor = ViewCompat.MEASURED_STATE_MASK;
    int touchId = -1;
    int width;

    public void nothing(View view) {
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.imagespiral_edit);
        getWindow().setFlags(1024, 1024);
        context = this;
        header = (TextView) findViewById(R.id.my_header_text);
        ivdone = (ImageView) findViewById(R.id.ivoption);
        releditor = (RelativeLayout) findViewById(R.id.releditor);
        relpbar = (RelativeLayout) findViewById(R.id.relpbar);
        ivblur = (ImageView) findViewById(R.id.ivblur);
        ivoriginal = (ImageView) findViewById(R.id.ivoriginal);
        ivcut = (ImageView) findViewById(R.id.ivcut);
        ivbspiral = (ImageView) findViewById(R.id.ivbspiral);
        ivfspiral = (ImageView) findViewById(R.id.ivfspiral);
        rcvspiral = (RecyclerView) findViewById(R.id.rcvspiral);
        ltext = (LinearLayout) findViewById(R.id.lineartext);
        reledit = (RelativeLayout) findViewById(R.id.reledit);
        ettext = (EditText) findViewById(R.id.ettext);
        ivcolor = (ImageView) findViewById(R.id.ivcolor);
        ivfont = (ImageView) findViewById(R.id.ivfont);
        ivtdone = (ImageView) findViewById(R.id.ivdone);
        relTextLayout = (RelativeLayout) findViewById(R.id.relTextLayout);
        lbg = (LinearLayout) findViewById(R.id.linearbg);
        lgradient = (LinearLayout) findViewById(R.id.lineargradient);
        rcvfilter = (RecyclerView) findViewById(R.id.rcvfilter);
        ivspiral = (ImageView) findViewById(R.id.ivspiral);
        ivfilter = (ImageView) findViewById(R.id.ivfilter);
        ivtext = (ImageView) findViewById(R.id.ivtext);
        ivbg = (ImageView) findViewById(R.id.ivbg);
        lbottom = (LinearLayout) findViewById(R.id.linearbottom);
        hsvbg = (HorizontalScrollView) findViewById(R.id.hsbg);
        ivgallery = (ImageView) findViewById(R.id.ivgallery);
        ivbgcolor = (ImageView) findViewById(R.id.ivbgcolor);
        ivgradient = (ImageView) findViewById(R.id.ivgradient);
        ivtexture = (ImageView) findViewById(R.id.ivtexture);
        ivgback = (ImageView) findViewById(R.id.ivgback);
        ivfirst = (ImageView) findViewById(R.id.ivfirst);
        ivsecond = (ImageView) findViewById(R.id.ivsecond);
        width = Helper.getWidth(context);
        height = Helper.getHeight(context);
        forUI();
        init();
    }

    private void forUI() {
        SetLayparam.setWidthAsBoth(context, releditor, 1080);
        SetLayparam.setMarginTop(context, releditor, 50);
        SetLayparam.setPadding(context, ltext, 0, 20, 0, 20);
        SetLayparam.setHeightWidth(context, reledit, 821, 110);
        SetLayparam.setPadding(context, ettext, 50, 0, 120, 0);
        SetLayparam.setHeightAsWidth(context, ivcolor, 100, 120);
        SetLayparam.setHeightAsWidth(context, ivfont, 100, 120);
        SetLayparam.setHeightAsWidth(context, ivtdone, 100, 120);
        SetLayparam.setMargins(context, ivtdone, 0, 0, 10, 0);
        SetLayparam.setMargins(context, rcvspiral, 0, 25, 0, 25);
        SetLayparam.setMargins(context, rcvfilter, 0, 25, 0, 25);
        SetLayparam.setPadding(context, lbg, 0, 15, 0, 15);
        SetLayparam.setPadding(context, lgradient, 0, 15, 0, 15);
        SetLayparam.setPaddingTop(context, lbottom, 35);
        SetLayparam.setHeightAsWidth(context, ivspiral, 150, 200);
        SetLayparam.setHeightAsWidth(context, ivfilter, 150, 200);
        SetLayparam.setHeightAsWidth(context, ivtext, 150, 200);
        SetLayparam.setHeightAsWidth(context, ivbg, 150, 200);
        SetLayparam.setHeightWidth(context, ivgallery, 302, 112);
        SetLayparam.setHeightWidth(context, ivbgcolor, 302, 112);
        SetLayparam.setHeightWidth(context, ivgradient, 302, 112);
        SetLayparam.setHeightWidth(context, ivtexture, 302, 112);
        SetLayparam.setMarginLeft(context, ivgallery, 15);
        SetLayparam.setMarginLeft(context, ivbgcolor, 15);
        SetLayparam.setMarginLeft(context, ivgradient, 15);
        SetLayparam.setMarginLeft(context, ivtexture, 15);
        SetLayparam.setHeightAsWidth(context, ivgback, 112, 112);
        SetLayparam.setHeightAsWidth(context, ivfirst, 411, 112);
        SetLayparam.setHeightAsWidth(context, ivsecond, 458, 112);
    }

    private void init() {
        header.setText(R.string.edit);
        ivdone.setVisibility(View.VISIBLE);
        header.setTypeface(Typeface.createFromAsset(getAssets(), "Poppins-Bold.ttf"));
        imgPath = getIntent().getStringExtra("crop");
        gcolor = new int[]{ViewCompat.MEASURED_STATE_MASK, -1};
        loadImages();
        initRecycler();
    }

    private void loadImages() {
        ((RequestBuilder) ((RequestBuilder) Glide.with(context).asBitmap().load(imgPath).centerCrop()).signature(new ObjectKey(Long.toString(System.currentTimeMillis())))).addListener(new RequestListener<Bitmap>() {
            public boolean onLoadFailed(@Nullable GlideException glideException, Object obj, Target<Bitmap> target, boolean z) {
                return false;
            }

            public boolean onResourceReady(Bitmap bitmap, Object obj, Target<Bitmap> target, DataSource dataSource, boolean z) {
                backBitmap = Bitmap.createBitmap(bitmap);
                Blurry.with(context).from(bitmap).into(ivblur);
                return true;
            }
        }).into(ivblur);
        ((RequestBuilder) Glide.with(context).load(imgPath).signature(new ObjectKey(Long.toString(System.currentTimeMillis())))).transition(DrawableTransitionOptions.withCrossFade()).into(ivoriginal);
        ((RequestBuilder) Glide.with(context).asBitmap().load(imgPath).signature(new ObjectKey(Long.toString(System.currentTimeMillis())))).into(new CustomTarget<Bitmap>() {
            public void onLoadCleared(@Nullable Drawable drawable) {
            }

            public void onResourceReady(@NonNull Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
                mainBitmap = Bitmap.createBitmap(bitmap);
                initializeCropping();
            }
        });
    }

    private void initRecycler() {
        rcvspiral.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
        rcvfilter.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
        adapter = new ImageSpiralAdapter(context, new ImageSpiralAdapter.OnSpiralListener() {
            public void onSpiralClicked(int i) {
                setSpiral(i);
            }
        });
        rcvspiral.setAdapter(adapter);
    }

    public void onFilterClicked(int i) {
        relpbar.setVisibility(View.VISIBLE);
        Bitmap filter = setFilter(i, backBitmap);
        Bitmap filter2 = setFilter(i, mainBitmap);
        Bitmap filter3 = setFilter(i, cropBitmap);
        ivblur.setImageBitmap(filter);
        ivoriginal.setImageBitmap(filter2);
        ivcut.setImageBitmap(filter3);
        relpbar.setVisibility(View.GONE);
    }

    @SuppressLint({"ClickableViewAccessibility"})
    public void onCropDone(Bitmap bitmap) {
        cropBitmap = Bitmap.createBitmap(bitmap);
        ivcut.setImageBitmap(cropBitmap);
        MultiTouchListener MultiTouchListener = new MultiTouchListener();
        MultiTouchListener.addSimultaneousView(ivbspiral);
        ivfspiral.setOnTouchListener(MultiTouchListener);
        setSpiral(0);
        new LoadFilter().execute(new Void[0]);
        backBitmap = getBitmapFromView(ivblur);
    }

    public void spiral(View view) {
        selectElement(celement == 0 ? -1 : 0);
    }

    public void text(View view) {
        int i = 1;
        if (celement == 1) {
            i = -1;
        }
        selectElement(i);
    }

    public void background(View view) {
        int i = 2;
        if (celement == 2) {
            i = -1;
        }
        selectElement(i);
    }

    public void filter(View view) {
        int i = 3;
        if (celement == 3) {
            i = -1;
        }
        selectElement(i);
    }

    public void gallery(View view) {
        selectElement(-1);
        Intent intent = new Intent("android.intent.action.PICK");
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, 101);
    }

    public void bgcolor(View view) {
        selectElement(-1);
        showColor(false, 1, bgcolor);
    }

    public void gradient(View view) {
        selectElement(23);
    }

    public void gradientBack(View view) {
        selectElement(2);
    }

    public void startColor(View view) {
        showColor(false, 2, gcolor[0]);
    }

    public void endColor(View view) {
        showColor(false, 3, gcolor[1]);
    }

    public void texture(View view) {
        selectElement(-1);
        startActivityForResult(new Intent(context, TextureList.class), 102);
    }

    public void option(View view) {
        new SaveImage().execute(new Void[0]);
    }

    public void textColor(View view) {
        showColor(false, 0, textcolor);
    }

    @SuppressLint({"InflateParams"})
    public void textFont(View view) {
        View inflate = ((LayoutInflater) context.getSystemService("layout_inflater")).inflate(R.layout.font_dialog, (ViewGroup) null, false);
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(inflate);
        ListView listView = (ListView) inflate.findViewById(R.id.spinlist);
        ImageView imageView = (ImageView) inflate.findViewById(R.id.ivclose);
        SetLayparam.setHeightWidth(context, (LinearLayout) inflate.findViewById(R.id.lineardialog), 969, 1056);
        SetLayparam.setHeight(context, (RelativeLayout) inflate.findViewById(R.id.reltitle), 150);
        SetLayparam.setHeightAsBoth(context, imageView, 90);
        SetLayparam.setMarginRight(context, imageView, 40);
        listView.setAdapter(new FontAdapter(context, Constant.font));
        imageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                Constant.fontname = Constant.font[i];
                PhotoEffectEditor PhotoEffectEditor1 = PhotoEffectEditor.this;
                AssetManager assets = context.getAssets();
                PhotoEffectEditor1.externalFont = Typeface.createFromAsset(assets, "fonts/" + Constant.fontname + ".ttf");
                ettext.setTypeface(externalFont);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void textDone(View view) {
        addText();
        selectElement(-1);
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i2 == -1) {
            switch (i) {
                case 101:
                    ((RequestBuilder) ((RequestBuilder) Glide.with(context).load(intent.getData()).centerCrop()).transition(DrawableTransitionOptions.withCrossFade()).signature(new ObjectKey(Long.toString(System.currentTimeMillis())))).listener(new RequestListener<Drawable>() {
                        public boolean onLoadFailed(@Nullable GlideException glideException, Object obj, Target<Drawable> target, boolean z) {
                            return false;
                        }

                        public boolean onResourceReady(Drawable drawable, Object obj, Target<Drawable> target, DataSource dataSource, boolean z) {
                            backBitmap = drawableToBitmap(drawable);
                            return false;
                        }
                    }).into(ivblur);
                    ivblur.setBackground((Drawable) null);
                    return;
                case 102:
                    ((RequestBuilder) ((RequestBuilder) Glide.with(context).load(intent.getStringExtra("texture")).centerCrop()).transition(DrawableTransitionOptions.withCrossFade()).signature(new ObjectKey(Long.toString(System.currentTimeMillis())))).listener(new RequestListener<Drawable>() {
                        public boolean onLoadFailed(@Nullable GlideException glideException, Object obj, Target<Drawable> target, boolean z) {
                            return false;
                        }

                        public boolean onResourceReady(Drawable drawable, Object obj, Target<Drawable> target, DataSource dataSource, boolean z) {
                            backBitmap = drawableToBitmap(drawable);
                            return false;
                        }
                    }).into(ivblur);
                    ivblur.setBackground((Drawable) null);
                    return;
                default:
                    return;
            }
        }
    }

    @SuppressLint({"ClickableViewAccessibility"})
    private void addText() {
        String obj = ettext.getText().toString();
        if (!TextUtils.isEmpty(obj)) {
            Bitmap textBitmap = Helper.getTextBitmap(obj, externalFont, textcolor);
            int height2 = textBitmap.getHeight();
            int width2 = textBitmap.getWidth();
            ImageView imageView = new ImageView(context);
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(width2, height2);
            imageView.setImageBitmap(textBitmap);
            imageView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    if (!isDoubleClick) {
                        isDoubleClick = true;
                        touchId = view.getId();
                    } else if (touchId == view.getId()) {
                        relTextLayout.removeView(view);
                        touchId = -1;
                    }
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            isDoubleClick = false;
                        }
                    }, 1000);
                }
            });
            imageView.setOnTouchListener(new MultiTouchListener());
            relTextLayout.addView(imageView, layoutParams);
            ettext.setText("");
            return;
        }
        Helper.show(context, "No text to add");
    }

    public void onSaveComplete(String str) {
        FBInterstitial.getInstance().displayFBInterstitial(PhotoEffectEditor.this, new FBInterstitial.FbCallback() {
            public void callbackCall() {
                Intent intent = new Intent(context, NeonPhotoPreview.class);
                intent.putExtra("from", 0);
                intent.putExtra("path", str);
                startActivity(intent);
            }
        });
    }

    public void setSpiral(int i) {
        String back = Path.SPIRAL.back(i);
        String front = Path.SPIRAL.front(i);
        Glide.with(context).load(back).transition(DrawableTransitionOptions.withCrossFade()).into(ivbspiral);
        Glide.with(context).load(front).transition(DrawableTransitionOptions.withCrossFade()).into(ivfspiral);
    }

    public Bitmap setFilter(int i, Bitmap bitmap) {
        Bitmap createBitmap = Bitmap.createBitmap(bitmap);
        Filter filter = new Filter();
        switch (i) {
            case 0:
                filter = SampleFilters.getBlueMessFilter();
                break;
            case 1:
                filter = SampleFilters.getLimeStutterFilter();
                break;
            case 2:
                filter = SampleFilters.getNightWhisperFilter();
                break;
            case 3:
                filter = SampleFilters.getStarLitFilter();
                break;
            case 4:
                filter = SampleFilters.getAweStruckVibeFilter();
                break;
            case 5:
                filter.addSubFilter(new SaturationSubFilter(3.0f));
                break;
            case 6:
                filter.addSubFilter(new ColorOverlaySubFilter(100, 0.2f, 0.2f, 0.0f));
                break;
            case 7:
                filter.addSubFilter(new ContrastSubFilter(3.0f));
                break;
            case 8:
                filter.addSubFilter(new BrightnessSubFilter(60));
                break;
            case 9:
                filter.addSubFilter(new VignetteSubFilter(context, 100));
                break;
            case 10:
                filter.addSubFilter(new ColorOverlaySubFilter(100, 0.2f, 0.0f, 0.0f));
                break;
        }
        return filter.processFilter(createBitmap);
    }

    public void selectElement(int i) {
        celement = i;
        if (i != 23) {
            switch (i) {
                case -1:
//                    ivspiral.setImageResource(R.drawable.btn_spiral);
//                    ivtext.setImageResource(R.drawable.btn_text);
//                    ivbg.setImageResource(R.drawable.btn_background);
//                    ivfilter.setImageResource(R.drawable.btn_filter);
                    rcvspiral.setVisibility(View.GONE);
                    rcvfilter.setVisibility(View.GONE);
                    ltext.setVisibility(View.GONE);
                    hsvbg.setVisibility(View.GONE);
                    lgradient.setVisibility(View.GONE);
                    return;
                case 0:
//                    ivspiral.setImageResource(R.drawable.spiral_press);
//                    ivtext.setImageResource(R.drawable.btn_text);
//                    ivbg.setImageResource(R.drawable.btn_background);
//                    ivfilter.setImageResource(R.drawable.btn_filter);
                    ltext.setVisibility(View.GONE);
                    hsvbg.setVisibility(View.GONE);
                    lgradient.setVisibility(View.GONE);
                    rcvfilter.setVisibility(View.GONE);
                    rcvspiral.setVisibility(View.VISIBLE);
                    return;
                case 1:
//                    ivspiral.setImageResource(R.drawable.btn_spiral);
//                    ivtext.setImageResource(R.drawable.text_press);
//                    ivbg.setImageResource(R.drawable.btn_background);
//                    ivfilter.setImageResource(R.drawable.btn_filter);
                    externalFont = null;
                    ettext.setTextColor(-1);
                    ettext.setTypeface((Typeface) null);
                    rcvspiral.setVisibility(View.GONE);
                    rcvfilter.setVisibility(View.GONE);
                    hsvbg.setVisibility(View.GONE);
                    lgradient.setVisibility(View.GONE);
                    ltext.setVisibility(View.VISIBLE);
                    return;
                case 2:
//                    ivspiral.setImageResource(R.drawable.btn_spiral);
//                    ivtext.setImageResource(R.drawable.btn_text);
//                    ivbg.setImageResource(R.drawable.background_press);
//                    ivfilter.setImageResource(R.drawable.btn_filter);
                    rcvspiral.setVisibility(View.GONE);
                    rcvfilter.setVisibility(View.GONE);
                    ltext.setVisibility(View.GONE);
                    lgradient.setVisibility(View.GONE);
                    hsvbg.setVisibility(View.VISIBLE);
                    return;
                case 3:
//                    ivspiral.setImageResource(R.drawable.btn_spiral);
//                    ivtext.setImageResource(R.drawable.btn_text);
//                    ivbg.setImageResource(R.drawable.btn_background);
//                    ivfilter.setImageResource(R.drawable.filter_press);
                    rcvspiral.setVisibility(View.GONE);
                    ltext.setVisibility(View.GONE);
                    lgradient.setVisibility(View.GONE);
                    hsvbg.setVisibility(View.GONE);
                    rcvfilter.setVisibility(View.VISIBLE);
                    return;
                default:
                    return;
            }
        } else {
            hsvbg.setVisibility(View.GONE);
            lgradient.setVisibility(View.VISIBLE);
        }
    }

    public void initializeCropping() {
        allredpath.clear();
        imageCutter = new ImageCutter();
        startProcessingAsyncBlur(mainBitmap, new HashSet());
    }

    public void startProcessingAsyncBlur(Bitmap bitmap, Set<PointF> set) {
        croptask = new AutoCrop(bitmap, set).execute(new Bitmap[]{bitmap});
    }

    public Bitmap getBitmapFromView(View view) {
        Bitmap createBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        canvas.drawColor(0);
        view.draw(canvas);
        return createBitmap;
    }

    public void showColor(boolean z, final int i1, int i2) {
        new AmbilWarnaDialog(context, Boolean.valueOf(z), i2, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            public void onOk(AmbilWarnaDialog AmbilWarnaDialog1, int i) {
                applyToType(i1, i);
                AmbilWarnaDialog1.getDialog().dismiss();
            }

            public void onCancel(AmbilWarnaDialog fX_AmbilWarnaDialog) {
                fX_AmbilWarnaDialog.getDialog().dismiss();
            }
        }).show();
    }

    public void applyToType(int i, int i2) {
        switch (i) {
            case 0:
                textcolor = i2;
                ettext.setTextColor(textcolor);
                return;
            case 1:
                bgcolor = i2;
                ivblur.setImageBitmap((Bitmap) null);
                ivblur.setBackgroundColor(bgcolor);
                backBitmap = getBitmapFromView(ivblur);
                return;
            case 2:
                gcolor[0] = i2;
                gapply();
                return;
            case 3:
                gcolor[1] = i2;
                gapply();
                return;
            default:
                return;
        }
    }

    public Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap;
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }
        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public void gapply() {
        if (gcolor.length < 2) {
            Helper.show(context, "Please select both color");
            return;
        }
        GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, gcolor);
        gradientDrawable.setGradientType(0);
        ivblur.setImageBitmap((Bitmap) null);
        ivblur.setBackground(gradientDrawable);
        backBitmap = getBitmapFromView(ivblur);
    }

    public int getWidth(int i) {
        return (width * i) / 1080;
    }

    public int getHeight(int i) {
        return (height * i) / 1920;
    }

    public void back(View view) {
        onBackPressed();
    }

    public void onBackPressed() {
        if (!(croptask == null || croptask.getStatus() == AsyncTask.Status.FINISHED)) {
            croptask.cancel(true);
        }
        super.onBackPressed();
        finish();
    }

    @SuppressLint({"StaticFieldLeak"})
    private class AutoCrop extends AsyncTask<Bitmap, Integer, Bitmap> {
        AssetManager assets;
        Bitmap bitmap;
        Set<PointF> set;

        private AutoCrop(Bitmap bitmap2, Set<PointF> set2) {
            assets = getAssets();
            bitmap = bitmap2;
            set = set2;
        }

        public void onPreExecute() {
            super.onPreExecute();
            relpbar.setVisibility(View.VISIBLE);
            System.gc();
        }

        public Bitmap doInBackground(Bitmap... bitmapArr) {
            if (!imageCutter.isInitialized()) {
                ImageCutter.initialize(assets, context);
            }
            Bitmap execute = imageCutter.execute(bitmap, set);
            ImageBlurMaker.doBlur(context, ImageUtils.scaleBitmap(execute, execute.getWidth() - getWidth(15), execute.getHeight() - getHeight(15)), 25, true);
            Bitmap mask = Helper.getMask(mainBitmap, execute);
            if (allredpath.size() <= 0 || RedCutBit == null) {
                int bitwidth = imageCutter.getBitwidth();
                int bitheight = imageCutter.getBitheight();
                RedCutBit = Bitmap.createBitmap(bitwidth, bitheight, Bitmap.Config.ARGB_8888);
                allredpath.clear();
                allredpath = new ArrayList<>(imageCutter.getAllredpath());
                Iterator<PointF> it = allredpath.iterator();
                while (it.hasNext()) {
                    PointF next = it.next();
                    if (!isCancelled()) {
                        RedCutBit.setPixel((int) next.x, (int) next.y, 0);
                    }
                }
                RedCutBit = ImageUtils.scaleBitmap(RedCutBit, mask.getWidth(), mask.getHeight());
            }
            return Helper.getMask(mainBitmap, Bitmap.createBitmap(mask));
        }

        public void onPostExecute(Bitmap bitmap2) {
            if (!isCancelled()) {
                onCropDone(Helper.getBitmapResize(context, bitmap2, 1080, 1080));
                relpbar.setVisibility(View.GONE);
            }
        }
    }

    @SuppressLint({"StaticFieldLeak"})
    public class SaveImage extends AsyncTask<Void, Void, Void> {
        String path;

        public SaveImage() {
        }

        public void onPreExecute() {
            super.onPreExecute();
            relpbar.setVisibility(View.VISIBLE);
        }

        public Void doInBackground(Void... voidArr) {
            path = Helper.saveMainBitmap(context, getBitmapFromView(releditor));
            return null;
        }

        public void onPostExecute(Void voidR) {
            super.onPostExecute(voidR);
            MediaScannerConnection.scanFile(context, new String[]{path}, new String[]{"jpg"}, new MediaScannerConnection.OnScanCompletedListener() {
                public void onScanCompleted(String str, Uri uri) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            relpbar.setVisibility(View.GONE);
                            onSaveComplete(SaveImage.this.path);
                        }
                    });
                }
            });
        }
    }

    @SuppressLint({"StaticFieldLeak"})
    public class LoadFilter extends AsyncTask<Void, Void, Void> {
        public LoadFilter() {
        }

        public void onPreExecute() {
            super.onPreExecute();
            albitmap.clear();
        }

        public Void doInBackground(Void... voidArr) {
            for (int i = 0; i < 11; i++) {
                albitmap.add(setFilter(i, mainBitmap));
            }
            return null;
        }

        public void onPostExecute(Void voidR) {
            super.onPostExecute(voidR);
            fadapter = new FilterAdapter(context, albitmap, PhotoEffectEditor.this);
            rcvfilter.setAdapter(fadapter);
        }
    }
}
