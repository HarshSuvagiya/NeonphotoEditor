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
import com.scorpion.NeonphotoEditor.AutoBgRemove.ImageBlurMaker;
import com.scorpion.NeonphotoEditor.AutoBgRemove.ImageCutter;
import com.scorpion.NeonphotoEditor.AutoBgRemove.ImageUtils;
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
        this.context = this;
        this.header = (TextView) findViewById(R.id.my_header_text);
        this.ivdone = (ImageView) findViewById(R.id.ivoption);
        this.releditor = (RelativeLayout) findViewById(R.id.releditor);
        this.relpbar = (RelativeLayout) findViewById(R.id.relpbar);
        this.ivblur = (ImageView) findViewById(R.id.ivblur);
        this.ivoriginal = (ImageView) findViewById(R.id.ivoriginal);
        this.ivcut = (ImageView) findViewById(R.id.ivcut);
        this.ivbspiral = (ImageView) findViewById(R.id.ivbspiral);
        this.ivfspiral = (ImageView) findViewById(R.id.ivfspiral);
        this.rcvspiral = (RecyclerView) findViewById(R.id.rcvspiral);
        this.ltext = (LinearLayout) findViewById(R.id.lineartext);
        this.reledit = (RelativeLayout) findViewById(R.id.reledit);
        this.ettext = (EditText) findViewById(R.id.ettext);
        this.ivcolor = (ImageView) findViewById(R.id.ivcolor);
        this.ivfont = (ImageView) findViewById(R.id.ivfont);
        this.ivtdone = (ImageView) findViewById(R.id.ivdone);
        this.relTextLayout = (RelativeLayout) findViewById(R.id.relTextLayout);
        this.lbg = (LinearLayout) findViewById(R.id.linearbg);
        this.lgradient = (LinearLayout) findViewById(R.id.lineargradient);
        this.rcvfilter = (RecyclerView) findViewById(R.id.rcvfilter);
        this.ivspiral = (ImageView) findViewById(R.id.ivspiral);
        this.ivfilter = (ImageView) findViewById(R.id.ivfilter);
        this.ivtext = (ImageView) findViewById(R.id.ivtext);
        this.ivbg = (ImageView) findViewById(R.id.ivbg);
        this.lbottom = (LinearLayout) findViewById(R.id.linearbottom);
        this.hsvbg = (HorizontalScrollView) findViewById(R.id.hsbg);
        this.ivgallery = (ImageView) findViewById(R.id.ivgallery);
        this.ivbgcolor = (ImageView) findViewById(R.id.ivbgcolor);
        this.ivgradient = (ImageView) findViewById(R.id.ivgradient);
        this.ivtexture = (ImageView) findViewById(R.id.ivtexture);
        this.ivgback = (ImageView) findViewById(R.id.ivgback);
        this.ivfirst = (ImageView) findViewById(R.id.ivfirst);
        this.ivsecond = (ImageView) findViewById(R.id.ivsecond);
        this.width = Helper.getWidth(this.context);
        this.height = Helper.getHeight(this.context);
        forUI();
        init();
    }

    private void forUI() {
        SetLayparam.setWidthAsBoth(this.context, this.releditor, 1080);
        SetLayparam.setMarginTop(this.context, this.releditor, 50);
        SetLayparam.setPadding(this.context, this.ltext, 0, 20, 0, 20);
        SetLayparam.setHeightWidth(this.context, this.reledit, 821, 110);
        SetLayparam.setPadding(this.context, this.ettext, 50, 0, 120, 0);
        SetLayparam.setHeightAsWidth(this.context, this.ivcolor, 100, 120);
        SetLayparam.setHeightAsWidth(this.context, this.ivfont, 100, 120);
        SetLayparam.setHeightAsWidth(this.context, this.ivtdone, 100, 120);
        SetLayparam.setMargins(this.context, this.ivtdone, 0, 0, 10, 0);
        SetLayparam.setMargins(this.context, this.rcvspiral, 0, 25, 0, 25);
        SetLayparam.setMargins(this.context, this.rcvfilter, 0, 25, 0, 25);
        SetLayparam.setPadding(this.context, this.lbg, 0, 15, 0, 15);
        SetLayparam.setPadding(this.context, this.lgradient, 0, 15, 0, 15);
        SetLayparam.setPaddingTop(this.context, this.lbottom, 35);
        SetLayparam.setHeightAsWidth(this.context, this.ivspiral, 150, 200);
        SetLayparam.setHeightAsWidth(this.context, this.ivfilter, 150, 200);
        SetLayparam.setHeightAsWidth(this.context, this.ivtext, 150, 200);
        SetLayparam.setHeightAsWidth(this.context, this.ivbg, 150, 200);
        SetLayparam.setHeightWidth(this.context, this.ivgallery, 302, 112);
        SetLayparam.setHeightWidth(this.context, this.ivbgcolor, 302, 112);
        SetLayparam.setHeightWidth(this.context, this.ivgradient, 302, 112);
        SetLayparam.setHeightWidth(this.context, this.ivtexture, 302, 112);
        SetLayparam.setMarginLeft(this.context, this.ivgallery, 15);
        SetLayparam.setMarginLeft(this.context, this.ivbgcolor, 15);
        SetLayparam.setMarginLeft(this.context, this.ivgradient, 15);
        SetLayparam.setMarginLeft(this.context, this.ivtexture, 15);
        SetLayparam.setHeightAsWidth(this.context, this.ivgback, 112, 112);
        SetLayparam.setHeightAsWidth(this.context, this.ivfirst, 411, 112);
        SetLayparam.setHeightAsWidth(this.context, this.ivsecond, 458, 112);
    }

    private void init() {
        this.header.setText(R.string.edit);
        this.ivdone.setVisibility(View.VISIBLE);
        this.header.setTypeface(Typeface.createFromAsset(getAssets(), "Poppins-Bold.ttf"));
        this.imgPath = getIntent().getStringExtra("crop");
        this.gcolor = new int[]{ViewCompat.MEASURED_STATE_MASK, -1};
        loadImages();
        initRecycler();
    }

    private void loadImages() {
        ((RequestBuilder) ((RequestBuilder) Glide.with(this.context).asBitmap().load(this.imgPath).centerCrop()).signature(new ObjectKey(Long.toString(System.currentTimeMillis())))).addListener(new RequestListener<Bitmap>() {
            public boolean onLoadFailed(@Nullable GlideException glideException, Object obj, Target<Bitmap> target, boolean z) {
                return false;
            }

            public boolean onResourceReady(Bitmap bitmap, Object obj, Target<Bitmap> target, DataSource dataSource, boolean z) {
                PhotoEffectEditor.this.backBitmap = Bitmap.createBitmap(bitmap);
                Blurry.with(PhotoEffectEditor.this.context).from(bitmap).into(PhotoEffectEditor.this.ivblur);
                return true;
            }
        }).into(this.ivblur);
        ((RequestBuilder) Glide.with(this.context).load(this.imgPath).signature(new ObjectKey(Long.toString(System.currentTimeMillis())))).transition(DrawableTransitionOptions.withCrossFade()).into(this.ivoriginal);
        ((RequestBuilder) Glide.with(this.context).asBitmap().load(this.imgPath).signature(new ObjectKey(Long.toString(System.currentTimeMillis())))).into(new CustomTarget<Bitmap>() {
            public void onLoadCleared(@Nullable Drawable drawable) {
            }

            public void onResourceReady(@NonNull Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
                PhotoEffectEditor.this.mainBitmap = Bitmap.createBitmap(bitmap);
                PhotoEffectEditor.this.initializeCropping();
            }
        });
    }

    private void initRecycler() {
        this.rcvspiral.setLayoutManager(new LinearLayoutManager(this.context, RecyclerView.HORIZONTAL, false));
        this.rcvfilter.setLayoutManager(new LinearLayoutManager(this.context, RecyclerView.HORIZONTAL, false));
        this.adapter = new ImageSpiralAdapter(this.context, new ImageSpiralAdapter.OnSpiralListener() {
            public void onSpiralClicked(int i) {
                PhotoEffectEditor.this.setSpiral(i);
            }
        });
        this.rcvspiral.setAdapter(this.adapter);
    }

    public void onFilterClicked(int i) {
        this.relpbar.setVisibility(View.VISIBLE);
        Bitmap filter = setFilter(i, this.backBitmap);
        Bitmap filter2 = setFilter(i, this.mainBitmap);
        Bitmap filter3 = setFilter(i, this.cropBitmap);
        this.ivblur.setImageBitmap(filter);
        this.ivoriginal.setImageBitmap(filter2);
        this.ivcut.setImageBitmap(filter3);
        this.relpbar.setVisibility(View.GONE);
    }

    @SuppressLint({"ClickableViewAccessibility"})
    public void onCropDone(Bitmap bitmap) {
        this.cropBitmap = Bitmap.createBitmap(bitmap);
        this.ivcut.setImageBitmap(this.cropBitmap);
        MultiTouchListener MultiTouchListener = new MultiTouchListener();
        MultiTouchListener.addSimultaneousView(this.ivbspiral);
        this.ivfspiral.setOnTouchListener(MultiTouchListener);
        setSpiral(0);
        new LoadFilter().execute(new Void[0]);
        this.backBitmap = getBitmapFromView(this.ivblur);
    }

    public void spiral(View view) {
        selectElement(this.celement == 0 ? -1 : 0);
    }

    public void text(View view) {
        int i = 1;
        if (this.celement == 1) {
            i = -1;
        }
        selectElement(i);
    }

    public void background(View view) {
        int i = 2;
        if (this.celement == 2) {
            i = -1;
        }
        selectElement(i);
    }

    public void filter(View view) {
        int i = 3;
        if (this.celement == 3) {
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
        showColor(false, 1, this.bgcolor);
    }

    public void gradient(View view) {
        selectElement(23);
    }

    public void gradientBack(View view) {
        selectElement(2);
    }

    public void startColor(View view) {
        showColor(false, 2, this.gcolor[0]);
    }

    public void endColor(View view) {
        showColor(false, 3, this.gcolor[1]);
    }

    public void texture(View view) {
        selectElement(-1);
        startActivityForResult(new Intent(this.context, TextureList.class), 102);
    }

    public void option(View view) {
        new SaveImage().execute(new Void[0]);
    }

    public void textColor(View view) {
        showColor(false, 0, this.textcolor);
    }

    @SuppressLint({"InflateParams"})
    public void textFont(View view) {
        View inflate = ((LayoutInflater) this.context.getSystemService("layout_inflater")).inflate(R.layout.font_dialog, (ViewGroup) null, false);
        final Dialog dialog = new Dialog(this.context);
        dialog.setContentView(inflate);
        ListView listView = (ListView) inflate.findViewById(R.id.spinlist);
        ImageView imageView = (ImageView) inflate.findViewById(R.id.ivclose);
        SetLayparam.setHeightWidth(this.context, (LinearLayout) inflate.findViewById(R.id.lineardialog), 969, 1056);
        SetLayparam.setHeight(this.context, (RelativeLayout) inflate.findViewById(R.id.reltitle), 150);
        SetLayparam.setHeightAsBoth(this.context, imageView, 90);
        SetLayparam.setMarginRight(this.context, imageView, 40);
        listView.setAdapter(new FontAdapter(this.context, Constant.font));
        imageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                Constant.fontname = Constant.font[i];
                PhotoEffectEditor PhotoEffectEditor1 = PhotoEffectEditor.this;
                AssetManager assets = PhotoEffectEditor.this.context.getAssets();
                PhotoEffectEditor1.externalFont = Typeface.createFromAsset(assets, "fonts/" + Constant.fontname + ".ttf");
                PhotoEffectEditor.this.ettext.setTypeface(PhotoEffectEditor.this.externalFont);
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
                    ((RequestBuilder) ((RequestBuilder) Glide.with(this.context).load(intent.getData()).centerCrop()).transition(DrawableTransitionOptions.withCrossFade()).signature(new ObjectKey(Long.toString(System.currentTimeMillis())))).listener(new RequestListener<Drawable>() {
                        public boolean onLoadFailed(@Nullable GlideException glideException, Object obj, Target<Drawable> target, boolean z) {
                            return false;
                        }

                        public boolean onResourceReady(Drawable drawable, Object obj, Target<Drawable> target, DataSource dataSource, boolean z) {
                            PhotoEffectEditor.this.backBitmap = PhotoEffectEditor.this.drawableToBitmap(drawable);
                            return false;
                        }
                    }).into(this.ivblur);
                    this.ivblur.setBackground((Drawable) null);
                    return;
                case 102:
                    ((RequestBuilder) ((RequestBuilder) Glide.with(this.context).load(intent.getStringExtra("texture")).centerCrop()).transition(DrawableTransitionOptions.withCrossFade()).signature(new ObjectKey(Long.toString(System.currentTimeMillis())))).listener(new RequestListener<Drawable>() {
                        public boolean onLoadFailed(@Nullable GlideException glideException, Object obj, Target<Drawable> target, boolean z) {
                            return false;
                        }

                        public boolean onResourceReady(Drawable drawable, Object obj, Target<Drawable> target, DataSource dataSource, boolean z) {
                            PhotoEffectEditor.this.backBitmap = PhotoEffectEditor.this.drawableToBitmap(drawable);
                            return false;
                        }
                    }).into(this.ivblur);
                    this.ivblur.setBackground((Drawable) null);
                    return;
                default:
                    return;
            }
        }
    }

    @SuppressLint({"ClickableViewAccessibility"})
    private void addText() {
        String obj = this.ettext.getText().toString();
        if (!TextUtils.isEmpty(obj)) {
            Bitmap textBitmap = Helper.getTextBitmap(obj, this.externalFont, this.textcolor);
            int height2 = textBitmap.getHeight();
            int width2 = textBitmap.getWidth();
            ImageView imageView = new ImageView(this.context);
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(width2, height2);
            imageView.setImageBitmap(textBitmap);
            imageView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    if (!PhotoEffectEditor.this.isDoubleClick) {
                        PhotoEffectEditor.this.isDoubleClick = true;
                        PhotoEffectEditor.this.touchId = view.getId();
                    } else if (PhotoEffectEditor.this.touchId == view.getId()) {
                        PhotoEffectEditor.this.relTextLayout.removeView(view);
                        PhotoEffectEditor.this.touchId = -1;
                    }
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            PhotoEffectEditor.this.isDoubleClick = false;
                        }
                    }, 1000);
                }
            });
            imageView.setOnTouchListener(new MultiTouchListener());
            this.relTextLayout.addView(imageView, layoutParams);
            this.ettext.setText("");
            return;
        }
        Helper.show(this.context, "No text to add");
    }

    public void onSaveComplete(String str) {
        Intent intent = new Intent(this.context, NeonPhotoPreview.class);
        intent.putExtra("from", 0);
        intent.putExtra("path", str);
        startActivity(intent);
    }

    public void setSpiral(int i) {
        String back = Path.SPIRAL.back(i);
        String front = Path.SPIRAL.front(i);
        Glide.with(this.context).load(back).transition(DrawableTransitionOptions.withCrossFade()).into(this.ivbspiral);
        Glide.with(this.context).load(front).transition(DrawableTransitionOptions.withCrossFade()).into(this.ivfspiral);
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
                filter.addSubFilter(new VignetteSubFilter(this.context, 100));
                break;
            case 10:
                filter.addSubFilter(new ColorOverlaySubFilter(100, 0.2f, 0.0f, 0.0f));
                break;
        }
        return filter.processFilter(createBitmap);
    }

    public void selectElement(int i) {
        this.celement = i;
        if (i != 23) {
            switch (i) {
                case -1:
//                    this.ivspiral.setImageResource(R.drawable.btn_spiral);
//                    this.ivtext.setImageResource(R.drawable.btn_text);
//                    this.ivbg.setImageResource(R.drawable.btn_background);
//                    this.ivfilter.setImageResource(R.drawable.btn_filter);
                    this.rcvspiral.setVisibility(View.GONE);
                    this.rcvfilter.setVisibility(View.GONE);
                    this.ltext.setVisibility(View.GONE);
                    this.hsvbg.setVisibility(View.GONE);
                    this.lgradient.setVisibility(View.GONE);
                    return;
                case 0:
//                    this.ivspiral.setImageResource(R.drawable.spiral_press);
//                    this.ivtext.setImageResource(R.drawable.btn_text);
//                    this.ivbg.setImageResource(R.drawable.btn_background);
//                    this.ivfilter.setImageResource(R.drawable.btn_filter);
                    this.ltext.setVisibility(View.GONE);
                    this.hsvbg.setVisibility(View.GONE);
                    this.lgradient.setVisibility(View.GONE);
                    this.rcvfilter.setVisibility(View.GONE);
                    this.rcvspiral.setVisibility(View.VISIBLE);
                    return;
                case 1:
//                    this.ivspiral.setImageResource(R.drawable.btn_spiral);
//                    this.ivtext.setImageResource(R.drawable.text_press);
//                    this.ivbg.setImageResource(R.drawable.btn_background);
//                    this.ivfilter.setImageResource(R.drawable.btn_filter);
                    this.externalFont = null;
                    this.ettext.setTextColor(-1);
                    this.ettext.setTypeface((Typeface) null);
                    this.rcvspiral.setVisibility(View.GONE);
                    this.rcvfilter.setVisibility(View.GONE);
                    this.hsvbg.setVisibility(View.GONE);
                    this.lgradient.setVisibility(View.GONE);
                    this.ltext.setVisibility(View.VISIBLE);
                    return;
                case 2:
//                    this.ivspiral.setImageResource(R.drawable.btn_spiral);
//                    this.ivtext.setImageResource(R.drawable.btn_text);
//                    this.ivbg.setImageResource(R.drawable.background_press);
//                    this.ivfilter.setImageResource(R.drawable.btn_filter);
                    this.rcvspiral.setVisibility(View.GONE);
                    this.rcvfilter.setVisibility(View.GONE);
                    this.ltext.setVisibility(View.GONE);
                    this.lgradient.setVisibility(View.GONE);
                    this.hsvbg.setVisibility(View.VISIBLE);
                    return;
                case 3:
//                    this.ivspiral.setImageResource(R.drawable.btn_spiral);
//                    this.ivtext.setImageResource(R.drawable.btn_text);
//                    this.ivbg.setImageResource(R.drawable.btn_background);
//                    this.ivfilter.setImageResource(R.drawable.filter_press);
                    this.rcvspiral.setVisibility(View.GONE);
                    this.ltext.setVisibility(View.GONE);
                    this.lgradient.setVisibility(View.GONE);
                    this.hsvbg.setVisibility(View.GONE);
                    this.rcvfilter.setVisibility(View.VISIBLE);
                    return;
                default:
                    return;
            }
        } else {
            this.hsvbg.setVisibility(View.GONE);
            this.lgradient.setVisibility(View.VISIBLE);
        }
    }

    public void initializeCropping() {
        this.allredpath.clear();
        this.imageCutter = new ImageCutter();
        startProcessingAsyncBlur(this.mainBitmap, new HashSet());
    }

    public void startProcessingAsyncBlur(Bitmap bitmap, Set<PointF> set) {
        this.croptask = new AutoCrop(bitmap, set).execute(new Bitmap[]{bitmap});
    }

    public Bitmap getBitmapFromView(View view) {
        Bitmap createBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        canvas.drawColor(0);
        view.draw(canvas);
        return createBitmap;
    }

    public void showColor(boolean z, final int i1, int i2) {
        new AmbilWarnaDialog(this.context, Boolean.valueOf(z), i2, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            public void onOk(AmbilWarnaDialog AmbilWarnaDialog1, int i) {
                PhotoEffectEditor.this.applyToType(i1, i);
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
                this.textcolor = i2;
                this.ettext.setTextColor(this.textcolor);
                return;
            case 1:
                this.bgcolor = i2;
                this.ivblur.setImageBitmap((Bitmap) null);
                this.ivblur.setBackgroundColor(this.bgcolor);
                this.backBitmap = getBitmapFromView(this.ivblur);
                return;
            case 2:
                this.gcolor[0] = i2;
                gapply();
                return;
            case 3:
                this.gcolor[1] = i2;
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
        if (this.gcolor.length < 2) {
            Helper.show(this.context, "Please select both color");
            return;
        }
        GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, this.gcolor);
        gradientDrawable.setGradientType(0);
        this.ivblur.setImageBitmap((Bitmap) null);
        this.ivblur.setBackground(gradientDrawable);
        this.backBitmap = getBitmapFromView(this.ivblur);
    }

    public int getWidth(int i) {
        return (this.width * i) / 1080;
    }

    public int getHeight(int i) {
        return (this.height * i) / 1920;
    }

    public void back(View view) {
        onBackPressed();
    }

    public void onBackPressed() {
        if (!(this.croptask == null || this.croptask.getStatus() == AsyncTask.Status.FINISHED)) {
            this.croptask.cancel(true);
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
            this.assets = PhotoEffectEditor.this.getAssets();
            this.bitmap = bitmap2;
            this.set = set2;
        }

        public void onPreExecute() {
            super.onPreExecute();
            PhotoEffectEditor.this.relpbar.setVisibility(View.VISIBLE);
            System.gc();
        }

        public Bitmap doInBackground(Bitmap... bitmapArr) {
            if (!PhotoEffectEditor.this.imageCutter.isInitialized()) {
                ImageCutter.initialize(this.assets, PhotoEffectEditor.this.context);
            }
            Bitmap execute = PhotoEffectEditor.this.imageCutter.execute(this.bitmap, this.set);
            ImageBlurMaker.doBlur(PhotoEffectEditor.this.context, ImageUtils.scaleBitmap(execute, execute.getWidth() - PhotoEffectEditor.this.getWidth(15), execute.getHeight() - PhotoEffectEditor.this.getHeight(15)), 25, true);
            Bitmap mask = Helper.getMask(PhotoEffectEditor.this.mainBitmap, execute);
            if (PhotoEffectEditor.this.allredpath.size() <= 0 || PhotoEffectEditor.this.RedCutBit == null) {
                int bitwidth = PhotoEffectEditor.this.imageCutter.getBitwidth();
                int bitheight = PhotoEffectEditor.this.imageCutter.getBitheight();
                PhotoEffectEditor.this.RedCutBit = Bitmap.createBitmap(bitwidth, bitheight, Bitmap.Config.ARGB_8888);
                PhotoEffectEditor.this.allredpath.clear();
                PhotoEffectEditor.this.allredpath = new ArrayList<>(PhotoEffectEditor.this.imageCutter.getAllredpath());
                Iterator<PointF> it = PhotoEffectEditor.this.allredpath.iterator();
                while (it.hasNext()) {
                    PointF next = it.next();
                    if (!isCancelled()) {
                        PhotoEffectEditor.this.RedCutBit.setPixel((int) next.x, (int) next.y, 0);
                    }
                }
                PhotoEffectEditor.this.RedCutBit = ImageUtils.scaleBitmap(PhotoEffectEditor.this.RedCutBit, mask.getWidth(), mask.getHeight());
            }
            return Helper.getMask(PhotoEffectEditor.this.mainBitmap, Bitmap.createBitmap(mask));
        }

        public void onPostExecute(Bitmap bitmap2) {
            if (!isCancelled()) {
                PhotoEffectEditor.this.onCropDone(Helper.getBitmapResize(PhotoEffectEditor.this.context, bitmap2, 1080, 1080));
                PhotoEffectEditor.this.relpbar.setVisibility(View.GONE);
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
            PhotoEffectEditor.this.relpbar.setVisibility(View.VISIBLE);
        }

        public Void doInBackground(Void... voidArr) {
            this.path = Helper.saveMainBitmap(PhotoEffectEditor.this.context, PhotoEffectEditor.this.getBitmapFromView(PhotoEffectEditor.this.releditor));
            return null;
        }

        public void onPostExecute(Void voidR) {
            super.onPostExecute(voidR);
            MediaScannerConnection.scanFile(PhotoEffectEditor.this.context, new String[]{this.path}, new String[]{"jpg"}, new MediaScannerConnection.OnScanCompletedListener() {
                public void onScanCompleted(String str, Uri uri) {
                    PhotoEffectEditor.this.runOnUiThread(new Runnable() {
                        public void run() {
                            PhotoEffectEditor.this.relpbar.setVisibility(View.GONE);
                            PhotoEffectEditor.this.onSaveComplete(SaveImage.this.path);
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
            PhotoEffectEditor.this.albitmap.clear();
        }

        public Void doInBackground(Void... voidArr) {
            for (int i = 0; i < 11; i++) {
                PhotoEffectEditor.this.albitmap.add(PhotoEffectEditor.this.setFilter(i, PhotoEffectEditor.this.mainBitmap));
            }
            return null;
        }

        public void onPostExecute(Void voidR) {
            super.onPostExecute(voidR);
            PhotoEffectEditor.this.fadapter = new FilterAdapter(PhotoEffectEditor.this.context, PhotoEffectEditor.this.albitmap, PhotoEffectEditor.this);
            PhotoEffectEditor.this.rcvfilter.setAdapter(PhotoEffectEditor.this.fadapter);
        }
    }
}
